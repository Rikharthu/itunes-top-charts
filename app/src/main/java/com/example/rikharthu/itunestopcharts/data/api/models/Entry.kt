package com.example.rikharthu.itunestopcharts.data.api.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tracks")
data class Entry(
        @PrimaryKey
        val id: String,
        val name: String,
        val artist: String,
        val title: String,
        val album: String,
        val previewUrl: String,
        val isFavorite:Boolean
//        ,
//        val images: List<Image>
)