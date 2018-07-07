package com.example.rikharthu.itunestopcharts

import com.example.rikharthu.itunestopcharts.data.api.models.Entry

object EntryDataFactory {

    fun makeEntry(): Entry {
        return Entry(DataFactory.randomString(), DataFactory.randomString(),
                DataFactory.randomString(), DataFactory.randomString(),
                DataFactory.randomString(), DataFactory.randomString(),
                false)
    }

    fun makgeFavoriteEntry(): Entry {
        return Entry(DataFactory.randomString(), DataFactory.randomString(),
                DataFactory.randomString(), DataFactory.randomString(),
                DataFactory.randomString(), DataFactory.randomString(),
                true)
    }
}