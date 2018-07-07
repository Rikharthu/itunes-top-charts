package com.example.rikharthu.itunestopcharts.repository

import android.arch.lifecycle.LiveData
import com.example.rikharthu.itunestopcharts.SingleLiveEvent
import com.example.rikharthu.itunestopcharts.data.api.models.Entry

open class TracksRemoteDataStore(private val tracksRemote: TracksRemote) : TracksDataStore {

    override fun getTracks(): LiveData<DataWrapper<List<Entry>>> {
        return tracksRemote.getTracks()
    }

    override fun saveTracks(tracks: List<Entry>): SingleLiveEvent<Void> {
        throw UnsupportedOperationException("Operation not supported")
    }

    override fun clearTracks(): SingleLiveEvent<Void> {
        throw UnsupportedOperationException("Operation not supported")
    }

    override fun getFavoriteTracks(): LiveData<DataWrapper<List<Entry>>> {
        throw UnsupportedOperationException("Operation not supported")
    }

    override fun setTrackAsFavorite(trackId: String): SingleLiveEvent<Void> {
        throw UnsupportedOperationException("Operation not supported")
    }

    override fun setTrackAsNotFavorite(trackid: String): SingleLiveEvent<Void> {
        throw UnsupportedOperationException("Operation not supported")
    }
}