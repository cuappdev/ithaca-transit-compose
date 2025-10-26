package com.cornellappdev.transit.models.ecosystem.gym

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.cornellappdev.transit.GymListQuery
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.util.ECOSYSTEM_FLAG
import com.cornellappdev.transit.util.ecosystem.toUpliftGyms
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for data related to eateries
 */
@Singleton
class GymRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val coroutineScope: CoroutineScope
) {

    private val gymQuery = apolloClient.query(GymListQuery())

    private val _gymFlow: MutableStateFlow<ApiResponse<List<UpliftGym>>> =
        MutableStateFlow(ApiResponse.Pending)


    /**
     * A StateFlow holding a list of all gyms
     */
    val gymFlow = _gymFlow.asStateFlow()

    private lateinit var activeGymJob: Job

    fun query() {
        if (::activeGymJob.isInitialized)
            activeGymJob.cancel()

        activeGymJob = coroutineScope.launch {
            gymQuery.toFlow().cancellable()
                .map {
                    val gymList = it.data?.getAllGyms?.filterNotNull()
                    if (gymList == null) {
                        ApiResponse.Error
                    } else {
                        ApiResponse.Success(gymList.flatMap { query -> query.toUpliftGyms() })
                    }
                }
                .catch {
                    Log.e("NetworkRequest", it.stackTraceToString())
                    emit(ApiResponse.Error)
                }.stateIn(
                    coroutineScope,
                    SharingStarted.Eagerly,
                    ApiResponse.Pending
                ).collect {
                    _gymFlow.emit(it)
                }
        }
    }

    init {
        if (ECOSYSTEM_FLAG) {
            query()
        }
    }

}