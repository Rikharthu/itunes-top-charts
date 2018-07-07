package com.example.rikharthu.itunestopcharts

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.support.annotation.UiThread
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.rikharthu.itunestopcharts.api.deserializers.FeedDeserializer
import com.example.rikharthu.itunestopcharts.data.api.models.Entry
import com.example.rikharthu.itunestopcharts.data.database.TracksDatabase
import com.example.rikharthu.itunestopcharts.repository.*
import com.google.gson.JsonParser
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import java.lang.IllegalStateException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun deserializes_correctly() {
        val json = InstrumentationRegistry.getContext().assets.open("feed_sample.json")
                .bufferedReader().use {
                    it.readText()
                }
        val parser = JsonParser()
        val jsonObj = parser.parse(json)

        val deserializer = FeedDeserializer()
        val feed = deserializer.parseFeed(jsonObj)

        println(feed)
    }

    @UiThread
    @Test
    fun does_something() {
        val database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getTargetContext(),
                TracksDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        val cache = TracksCacheImpl(database)
        val cacheStore = TracksCacheDataStore(cache)
        val entries = listOf(EntryDataFactory.makeEntry(), EntryDataFactory.makeEntry())
        val tracksRemote = object : TracksRemote {
            override fun getTracks(): LiveData<DataWrapper<List<Entry>>> {
                val tracksData = MutableLiveData<DataWrapper<List<Entry>>>()
                tracksData.value = DataWrapper(entries)
                return tracksData
            }
        }

        val tracksRemoteStore = TracksRemoteDataStore(tracksRemote)

        val repository = TracksDataRepository(cache, cacheStore, tracksRemoteStore)

        val result = repository.getTracks().blockingObserve()
        val dbResults = database.tracksDao().getTracks().blockingObserve()

        assertEquals(entries, result?.data)
        assertEquals(entries, dbResults)
    }

    @Test
    fun getTracks_isCachedAndCacheNotExpired_returnsCachedTracks() {
        val entries = listOf(EntryDataFactory.makeEntry(), EntryDataFactory.makeEntry())
        val cache = mock(TracksCache::class.java)
        `when`(cache.areTracksCached()).thenReturn(true.asLiveData())
        `when`(cache.isTracksCacheExpired()).thenReturn(false.asLiveData())
        val cacheStore = mock(TracksCacheDataStore::class.java)
        `when`(cacheStore.getTracks()).thenReturn(DataWrapper(entries).asLiveData())
        val tracksRemoteStore = mock(TracksRemoteDataStore::class.java)
        `when`(tracksRemoteStore.getTracks()).thenThrow(IllegalStateException("This should not be called"))

        val repository = TracksDataRepository(cache, cacheStore, tracksRemoteStore)

        val result = repository.getTracks().blockingObserve()

        // Repository did not request data from remote
        verify(tracksRemoteStore, never()).getTracks()
        // Data is same as provided by cache
        assertEquals(entries, result?.data)
    }


    @Test
    fun getTracks_isCachedAndCacheIsExpired_returnsRemoteTracks() {
        val entries = listOf(EntryDataFactory.makeEntry(), EntryDataFactory.makeEntry())
        val cache = mock(TracksCache::class.java)
        `when`(cache.areTracksCached()).thenReturn(true.asLiveData())
        // Is expired
        `when`(cache.isTracksCacheExpired()).thenReturn(true.asLiveData())
        val cacheStore = mock(TracksCacheDataStore::class.java)
        `when`(cacheStore.getTracks()).thenReturn(DataWrapper(entries).asLiveData())
        val tracksRemoteStore = mock(TracksRemoteDataStore::class.java)
        `when`(tracksRemoteStore.getTracks()).thenReturn(DataWrapper(entries).asLiveData())

        val repository = TracksDataRepository(cache, cacheStore, tracksRemoteStore)

        repository.getTracks().blockingObserve()

        verify(tracksRemoteStore).getTracks()
    }

    @Test
    fun getTracks_isNotCached_returnsRemoteTracks() {
        val entries = listOf(EntryDataFactory.makeEntry(), EntryDataFactory.makeEntry())
        val cache = mock(TracksCache::class.java)
        // Is not cached
        `when`(cache.areTracksCached()).thenReturn(false.asLiveData())
        `when`(cache.isTracksCacheExpired()).thenReturn(DataFactory.randomBoolean().asLiveData())
        val cacheStore = mock(TracksCacheDataStore::class.java)
        `when`(cacheStore.getTracks()).thenReturn(DataWrapper(entries).asLiveData())
        val tracksRemoteStore = mock(TracksRemoteDataStore::class.java)
        `when`(tracksRemoteStore.getTracks()).thenReturn(DataWrapper(entries).asLiveData())

        val repository = TracksDataRepository(cache, cacheStore, tracksRemoteStore)

        repository.getTracks().blockingObserve()

        verify(tracksRemoteStore).getTracks()
    }
}
