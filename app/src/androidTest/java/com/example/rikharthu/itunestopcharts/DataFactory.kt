package com.example.rikharthu.itunestopcharts

import java.util.*
import java.util.concurrent.ThreadLocalRandom

object DataFactory {

    fun randomString(): String {
        return UUID.randomUUID().toString()
    }

    fun randomInt(): Int {
        return ThreadLocalRandom.current().nextInt()
    }

    fun randomBoolean(): Boolean {
        return Math.random() < 0.5
    }
}