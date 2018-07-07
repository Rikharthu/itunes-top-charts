package com.example.rikharthu.itunestopcharts.repository

import android.arch.lifecycle.LiveData
import com.example.rikharthu.itunestopcharts.data.api.models.Entry

interface TracksRemote {

    fun getTracks(): LiveData<DataWrapper<List<Entry>>>
}