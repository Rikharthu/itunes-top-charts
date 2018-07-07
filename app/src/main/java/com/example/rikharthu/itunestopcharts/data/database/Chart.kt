package com.example.rikharthu.itunestopcharts.data.database

import java.util.*

data class Chart(
        val title: String,
        val rights: String,
        val updated: Date,
        val author: String,
        val tracks: List<Track> // TODO do not save, managed by join table?
        // TODO better store track ids. Only chart should know about it's track. Tracks should not know about chart they are in
)