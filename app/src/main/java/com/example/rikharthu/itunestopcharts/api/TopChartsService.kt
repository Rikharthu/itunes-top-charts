package com.example.rikharthu.itunestopcharts.api

import com.example.rikharthu.itunestopcharts.data.api.models.Feed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TopChartsService {

    @GET("/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit={limit}/json")
    fun getTopTracks(@Path(value = "limit") limit: Int = 10): Call<Feed>
}