package com.example.findplacenearlocation.api

import com.example.findplacenearlocation.model.ResponseBean
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceApi {

    companion object {
        private const val CLIENT_ID = "IK4QB0KWMKJMNIFDXJY5X51VUP4NPXSPV0K12D4Z3D5YZUQZ"
        private const val CLIENT_SECRET = "PGSIDON1C2TSKTSDNKIK4G2GSMCRMK1AXTKUJZWUU0GRWWEL"
        private const val VERSION = "20180401"
        private const val COMMON_PARAMS = "client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET&v=$VERSION"
    }

    @GET("v2/venues/search?$COMMON_PARAMS")
    suspend fun getNearPlaces(
        @Query("ll") latLang: String,
        @Query("limit") limit: Int = 20
    ): Response<ResponseBean>

    @GET("v2/venues/{venue_id}")
    suspend fun getDetailPlace(
        @Query("venue_id") venue_id: String,
        @Query("client_id") client_id: String = CLIENT_ID,
        @Query("client_secret") client_secret: String = CLIENT_SECRET
    ): Response<ResponseBean>
}