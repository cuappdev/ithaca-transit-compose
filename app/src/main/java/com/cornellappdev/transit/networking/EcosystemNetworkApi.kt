package com.cornellappdev.transit.networking

import com.cornellappdev.transit.models.Library
import com.cornellappdev.transit.models.Payload
import com.cornellappdev.transit.models.Printer
import retrofit2.http.GET

interface EcosystemNetworkApi {

    @GET("/api/v1/printers")
    suspend fun getPrinters(): Payload<List<Printer>>

    @GET("/api/v1/libraries")
    suspend fun getLibraries(): Payload<List<Library>>

}