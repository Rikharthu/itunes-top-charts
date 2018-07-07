package com.example.rikharthu.itunestopcharts.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.rikharthu.itunestopcharts.data.api.models.Entry

@Database(entities = [Entry::class], version = 1)
abstract class TracksDatabase : RoomDatabase() {

    abstract fun tracksDao(): TracksDao
}