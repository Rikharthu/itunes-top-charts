package com.example.rikharthu.itunestopcharts.repository

import android.arch.lifecycle.LiveData
import com.example.rikharthu.itunestopcharts.SingleLiveEvent
import com.example.rikharthu.itunestopcharts.data.api.models.Entry

interface TracksRepository {

    fun getTracks(forceRefresh: Boolean = false): LiveData<DataWrapper<List<Entry>>>

    fun favoriteTrack(trackId: String): SingleLiveEvent<Void>

    fun unfavoriteTrack(trackId: String): SingleLiveEvent<Void>

    fun getFavoriteTracks(): LiveData<DataWrapper<List<Entry>>>
}