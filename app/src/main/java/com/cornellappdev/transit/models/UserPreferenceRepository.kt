package com.cornellappdev.transit.models


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Repository to store data related to user preferences
 */
@Singleton
class UserPreferenceRepository @Inject constructor(@ApplicationContext val context: Context) {

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
        val RECENTS_MAP = stringSetPreferencesKey("recents")
    }

    /**
     * Configuring serializer
     */
    private val json = Json { encodeDefaults = true }

    /**
     * Sets favorites in user preferences
     * @param favorites: The set containing names of stops that have been favorites
     */
    suspend fun setFavorites(favorites: Set<Place>) {
        dataStore.edit { preferences ->
            val favoriteStrings = favorites.map { json.encodeToString(it) }.toSet()
            preferences[FAVORITES_MAP] = favoriteStrings
        }
    }

    /**
     * Flow of favorite stops
     */
    val favoritesFlow: StateFlow<Set<Place>> = dataStore.data
        .catch {
            setOf<Place>()
        }
        .map { preferences ->
            val favoriteStrings = preferences[FAVORITES_MAP] ?: setOf()
            val response = favoriteStrings.mapNotNull { jsonString ->
                try {
                    json.decodeFromString<Place>(jsonString)
                } catch (e: Exception) {
                    null
                }
            }.toSet()
            response
        }.stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.Eagerly, emptySet())

    /**
     * Adds a recent in user preferences
     * @param recents: The set containing names of stops that have been searched recently
     */
    suspend fun setRecents(recents: Set<Place>) {
        dataStore.edit { preferences ->
            val recentStrings = recents.map { json.encodeToString(it) }.toSet()
            preferences[RECENTS_MAP] = recentStrings
        }
    }

    /**
     * Flow of recently searched stops
     */
    val recentsFlow: StateFlow<Set<Place>> = dataStore.data.catch {
        setOf<Place>()
    }.map { preferences ->
        val recentStrings = preferences[RECENTS_MAP] ?: setOf()
        val response = recentStrings.mapNotNull { jsonString ->
            try {
                json.decodeFromString<Place>(jsonString)
            } catch (e: Exception) {
                null
            }
        }.toSet()
        response
    }.stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.Eagerly, emptySet())
}