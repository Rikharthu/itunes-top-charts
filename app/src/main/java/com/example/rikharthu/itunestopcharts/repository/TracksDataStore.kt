package com.example.rikharthu.itunestopcharts.repository

import android.arch.lifecycle.LiveData
import com.example.rikharthu.itunestopcharts.SingleLiveEvent
import com.example.rikharthu.itunestopcharts.data.api.models.Entry

interface TracksDataStore {

    fun getTracks(): LiveData<DataWrapper<List<Entry>>>

    fun saveTracks(tracks: List<Entry>): SingleLiveEvent<Void>

    fun clearTracks(): SingleLiveEvent<Void>

    fun getFavoriteTracks(): LiveData<DataWrapper<List<Entry>>>

    fun setTrackAsFavorite(trackId: String): SingleLiveEvent<Void>

    fun setTrackAsNotFavorite(trackid: String): SingleLiveEvent<Void>
}