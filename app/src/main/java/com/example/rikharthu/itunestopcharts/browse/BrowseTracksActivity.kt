package com.example.rikharthu.itunestopcharts.browse

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.rikharthu.itunestopcharts.R
import com.example.rikharthu.itunestopcharts.api.TopChartsService
import com.example.rikharthu.itunestopcharts.api.deserializers.FeedDeserializer
import com.example.rikharthu.itunestopcharts.data.Resource
import com.example.rikharthu.itunestopcharts.data.api.models.Entry
import com.example.rikharthu.itunestopcharts.data.api.models.Feed
import com.example.rikharthu.itunestopcharts.data.database.TracksDatabase
import com.example.rikharthu.itunestopcharts.repository.*
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.text.DateFormat

class BrowseTracksActivity : AppCompatActivity() {

    private lateinit var viewModel: BrowseTracksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_tracks)

        val gson = GsonBuilder()
                .setLenient()
                .registerTypeAdapter(
                        Feed::class.java, FeedDeserializer()
                )
                .setDateFormat(DateFormat.FULL, DateFormat.FULL)
                .create()
        val retrofit = Retrofit.Builder()
                .client(OkHttpClient())
                .baseUrl("http://ax.itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val apiService = retrofit.create(TopChartsService::class.java)

        val database = Room.inMemoryDatabaseBuilder(
                applicationContext,
                TracksDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        val cache = TracksCacheImpl(database)
        val cacheStore = TracksCacheDataStore(cache)
        val remote = TracksRemoteImpl(apiService)
        val remoteStore = TracksRemoteDataStore(remote)
        val repo = TracksDataRepository(cache, cacheStore, remoteStore)

        viewModel = BrowseTracksViewModel(repo)
        viewModel.tracks.observe(this, Observer {
            it?.let {
                onTracksChanged(it)
            }
        })
    }

    private fun onTracksChanged(tracks: Resource<List<Entry>>) {
        Timber.d("Tracks changed: $tracks")
    }
}
