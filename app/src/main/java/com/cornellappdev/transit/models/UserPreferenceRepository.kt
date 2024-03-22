package com.cornellappdev.transit.models


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class UserPreferenceRepository(context: Context) {

    /**
     * User Preferences file for Transit
     */
    private val Context.dataStore by preferencesDataStore(
        name = "UserPreferences"
    )

    private val dataStore = context.dataStore

    /**
     * Key values for the preferences stored in User Preferences
     */
    companion object PreferencesKeys {
        val FAVORITES_MAP = stringSetPreferencesKey("favorites")
    }

    /**
     * Sets favorites in user preferences
     * @param favorites: The set containing names of stops that have been favorites
     */
    suspend fun setFavorites(favorites: Set<String>) {
        dataStore.edit { preferences ->
            preferences[FAVORITES_MAP] = favorites
        }
    }

    /**
     * Flow of favorite stops
     */
    val favoritesFlow: StateFlow<Set<String>> = dataStore.data
        .catch {
            val empty: Set<String> = setOf()
            empty
        }
        .map { preferences ->
            val empty: Set<String> = setOf()
            val favorites = preferences[FAVORITES_MAP] ?: empty
            favorites
        }.stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.Eagerly, emptySet())

}