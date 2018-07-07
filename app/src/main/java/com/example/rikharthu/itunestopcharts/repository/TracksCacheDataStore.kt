package com.example.rikharthu.itunestopcharts.repository

import android.arch.lifecycle.LiveData
import com.example.rikharthu.itunestopcharts.SingleLiveEvent
import com.example.rikharthu.itunestopcharts.data.api.models.Entry

open class TracksCacheDataStore(
        private val tracksCache: TracksCache
) : TracksDataStore {

    override fun getTracks(): LiveData<DataWrapper<List<Entry>>> {
        return tracksCache.getTracks()
    }

    override fun saveTracks(tracks: List<Entry>): SingleLiveEvent<Void> {
        return SingleLiveEvent<Void>().apply {
            tracksCache.saveTracks(tracks)
            call()
        }
    }

    override fun clearTracks(): SingleLiveEvent<Void> {
        return SingleLiveEvent<Void>().apply {
            tracksCache.clearTracks()
            call()
        }
    }

    override fun getFavoriteTracks(): LiveData<DataWrapper<List<Entry>>> {
        return tracksCache.getFavoriteTracks()
    }

    override fun setTrackAsFavorite(trackId: String): SingleLiveEvent<Void> {
        return SingleLiveEvent<Void>().apply {
            tracksCache.setTrackAsFavorite(trackId)
            call()
        }
    }

    override fun setTrackAsNotFavorite(trackId: String): SingleLiveEvent<Void> {
        return SingleLiveEvent<Void>().apply {
            tracksCache.setTrackAsNotFavorite(trackId)
            call()
        }
    }
}