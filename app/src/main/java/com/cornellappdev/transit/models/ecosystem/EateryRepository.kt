package com.cornellappdev.transit.models.ecosystem

import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.networking.EateryNetworkApi
import com.cornellappdev.transit.util.ECOSYSTEM_FLAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Repository for data related to eateries
 */
@Singleton
class EateryRepository @Inject constructor(
    private val eateryNetworkApi: EateryNetworkApi,
) {

    private suspend fun getEateries(): List<Eatery> = eateryNetworkApi.getEateries()

    private val _eateryFlow: MutableStateFlow<ApiResponse<List<Eatery>>> =
        MutableStateFlow(ApiResponse.Pending)

    init {
        if (ECOSYSTEM_FLAG) {
            fetchAllEateries()
        }
    }

    /**
     * A StateFlow holding a list of all eateries
     */
    val eateryFlow = _eateryFlow.asStateFlow()


    /**
     * Makes a new call to backend for all eateries
     */
    private fun fetchAllEateries() {
        _eateryFlow.value = ApiResponse.Pending
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val rideResponse = getEateries()
                _eateryFlow.value = ApiResponse.Success(rideResponse)
            } catch (e: Exception) {
                _eateryFlow.value = ApiResponse.Error
            }
        }
    }

}