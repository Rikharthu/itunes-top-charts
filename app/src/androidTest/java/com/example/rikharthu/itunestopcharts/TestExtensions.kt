package com.example.rikharthu.itunestopcharts

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


fun <T> LiveData<T>.blockingObserve(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)
    val innerObserver = Observer<T> {
        value = it
        latch.countDown()
    }
    observeForever(innerObserver)
    latch.await(5, TimeUnit.SECONDS)
    return value
}

fun <T> T.asLiveData(): LiveData<T> {
    return MutableLiveData<T>().apply {
        postValue(this@asLiveData)
    }
}