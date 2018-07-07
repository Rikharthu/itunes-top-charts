package com.example.rikharthu.itunestopcharts.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.example.rikharthu.itunestopcharts.LiveDataUtils
import com.example.rikharthu.itunestopcharts.SingleLiveEvent
import com.example.rikharthu.itunestopcharts.data.api.models.Entry
import timber.log.Timber

class TracksDataRepository(
        private val cache: TracksCache,
        private val cacheStore: TracksCacheDataStore,
        private val remote: TracksRemoteDataStore
) : TracksRepository {

    override fun getTracks(forceRefresh: Boolean): LiveData<DataWrapper<List<Entry>>> {
        Timber.d("getTracks called")
        return Transformations.switchMap(LiveDataUtils.zip(cache.areTracksCached(),
                cache.isTracksCacheExpired()))
        {
            // First check whether there are cached tracks and cache is not expired
            val (isCached, isCacheExpired) = it
            val useCache = !forceRefresh and isCached and !isCacheExpired
            Timber.d("isCached=$isCached, isCacheExpired=$isCacheExpired, useCache=$useCache")
            return@switchMap if (useCache) {
                // Return directly from database
                Timber.d("Returning cached tracks")
                cacheStore.getTracks()
            } else {
                Timber.d("Fetching tracks from remote")
                // Fetch new data, insert it into database and return from database
                Transformations.switchMap(remote.getTracks()) {
                    var isSuccess = false
                    if (it.data != null) {
                        isSuccess = true
                        Timber.d("Saving tracks")
                        // TODO wipe non-favorited tracks
                        cacheStore.clearTracks()
                        cacheStore.saveTracks(it.data)
                    }
                    Timber.d("Returning newly saved tracks")
                    Transformations.map(cacheStore.getTracks()) {
                        return@map if (isSuccess) {
                            it
                        } else {
                            DataWrapper(it.data, Throwable("Could not refresh data"))
                        }
                    }
                }
            }
        }
    }

    override fun favoriteTrack(trackId: String): SingleLiveEvent<Void> {
        return SingleLiveEvent<Void>().apply {
            cacheStore.setTrackAsFavorite(trackId)
            call()
        }
    }

    override fun unfavoriteTrack(trackId: String): SingleLiveEvent<Void> {
        return SingleLiveEvent<Void>().apply {
            cacheStore.setTrackAsNotFavorite(trackId)
            call()
        }
    }

    override fun getFavoriteTracks(): LiveData<DataWrapper<List<Entry>>> {
        return cacheStore.getFavoriteTracks()
    }
}