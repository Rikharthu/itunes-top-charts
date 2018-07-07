package com.example.rikharthu.itunestopcharts.repository

import android.arch.lifecycle.LiveData
import com.example.rikharthu.itunestopcharts.SingleLiveEvent
import com.example.rikharthu.itunestopcharts.data.api.models.Entry

interface TracksCache {

    fun clearTracks(): SingleLiveEvent<Void>

    fun saveTracks(tracks: List<Entry>): SingleLiveEvent<Void>

    fun getTracks(): LiveData<DataWrapper<List<Entry>>>

    fun getFavoriteTracks(): LiveData<DataWrapper<List<Entry>>>

    fun setTrackAsFavorite(trackId: String): SingleLiveEvent<Void>

    fun setTrackAsNotFavorite(trackId: String): SingleLiveEvent<Void>

    fun areTracksCached(): LiveData<Boolean>

    fun isTracksCacheExpired(): LiveData<Boolean>
}