package com.example.rikharthu.itunestopcharts.browse

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.example.rikharthu.itunestopcharts.data.Resource
import com.example.rikharthu.itunestopcharts.data.api.models.Entry
import com.example.rikharthu.itunestopcharts.repository.TracksRepository

class BrowseTracksViewModel(private val repository: TracksRepository) : ViewModel() {

    val tracks: LiveData<Resource<List<Entry>>>

    init {
        tracks = Transformations.map(repository.getTracks()) {
            if (it.data != null && it.error == null) {
                return@map Resource.success(it.data)
            } else {
                return@map Resource(status = Resource.Status.ERROR, data = it.data)
            }
        }
    }

    fun refresh() {
        repository.getTracks(forceRefresh = true)
    }
}