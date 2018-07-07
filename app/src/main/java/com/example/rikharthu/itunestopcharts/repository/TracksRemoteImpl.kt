package com.example.rikharthu.itunestopcharts.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.rikharthu.itunestopcharts.api.TopChartsService
import com.example.rikharthu.itunestopcharts.data.api.models.Entry
import com.example.rikharthu.itunestopcharts.data.api.models.Feed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TracksRemoteImpl(private val apiService: TopChartsService) : TracksRemote {
    override fun getTracks(): LiveData<DataWrapper<List<Entry>>> {
        val liveData = MutableLiveData<DataWrapper<List<Entry>>>()

        val call = apiService.getTopTracks()
        call.enqueue(object : Callback<Feed> {
            override fun onFailure(call: Call<Feed>, t: Throwable) {
                liveData.value = DataWrapper(error = t)
            }

            override fun onResponse(call: Call<Feed>, response: Response<Feed>) {
                if (response.isSuccessful && response.body() != null) {
                    liveData.value = DataWrapper(data = response.body()!!.list)
                } else {
                    liveData.value = DataWrapper(error = Throwable("Response body was empty"))
                }
            }
        })
        return liveData
    }
}