package com.example.rikharthu.itunestopcharts.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.example.rikharthu.itunestopcharts.LiveDataTransformations
import com.example.rikharthu.itunestopcharts.SingleLiveEvent
import com.example.rikharthu.itunestopcharts.asLiveData
import com.example.rikharthu.itunestopcharts.data.api.models.Entry
import com.example.rikharthu.itunestopcharts.data.database.TracksDatabase

class TracksCacheImpl(private val database: TracksDatabase) : TracksCache {

    override fun clearTracks(): SingleLiveEvent<Void> {
        return SingleLiveEvent<Void>().apply {
            database.tracksDao().deleteTracks()
            call()
        }
    }

    override fun saveTracks(tracks: List<Entry>): SingleLiveEvent<Void> {
        return SingleLiveEvent<Void>().apply {
            database.tracksDao().insertTracks(*tracks.toTypedArray())
            call()
        }
    }

    override fun getTracks(): LiveData<DataWrapper<List<Entry>>> {
        return database.tracksDao().getTracks().wrapData()
    }

    override fun getFavoriteTracks(): LiveData<DataWrapper<List<Entry>>> {
        return database.tracksDao().getFavoriteTracks().wrapData()
    }

    override fun setTrackAsFavorite(trackId: String): SingleLiveEvent<Void> {
        return SingleLiveEvent<Void>().apply {
            database.tracksDao().setFavoriteStatus(true, trackId)
            call()
        }
    }

    override fun setTrackAsNotFavorite(trackId: String): SingleLiveEvent<Void> {
        return SingleLiveEvent<Void>().apply {
            database.tracksDao().setFavoriteStatus(false, trackId)
            call()
        }
    }

    override fun areTracksCached(): LiveData<Boolean> {
        return LiveDataTransformations.single(Transformations.map(database.tracksDao().getTracks()) {
            return@map it.isNotEmpty()
        })
    }

    override fun isTracksCacheExpired(): LiveData<Boolean> {
        // TODO implement correctly
        return true.asLiveData()
    }

    private fun <T> LiveData<T>.wrapData(): LiveData<DataWrapper<T>> {
        return Transformations.map(this) {
            return@map DataWrapper(data = it)
        }
    }
}