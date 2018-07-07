package com.example.rikharthu.itunestopcharts.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.rikharthu.itunestopcharts.data.api.models.Entry

@Dao
abstract class TracksDao {

    @Query("SELECT * FROM tracks")
    abstract fun getTracks(): LiveData<List<Entry>>

    @Query("DELETE FROM tracks")
    abstract fun deleteTracks()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTracks(vararg tracks: Entry)

    @Query("SELECT * FROM tracks") // TODO
    abstract fun getFavoriteTracks(): LiveData<List<Entry>>

    @Query("UPDATE tracks SET isFavorite = :isFavorite WHERE id = :trackId")
    abstract fun setFavoriteStatus(isFavorite: Boolean, trackId: String)
}