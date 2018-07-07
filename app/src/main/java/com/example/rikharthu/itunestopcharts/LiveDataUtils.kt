package com.example.rikharthu.itunestopcharts

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData

object LiveDataUtils {

    fun <A, B> zip(a: LiveData<A>, b: LiveData<B>): LiveData<Pair<A, B>> {
        return MediatorLiveData<Pair<A, B>>().apply {
            var first: A? = null
            var second: B? = null

            fun update() {
                val localFirst = first
                val localSecond = second
                if (localFirst != null && localSecond != null)
                    postValue(Pair(localFirst, localSecond))
            }

            addSource(a) {
                first = it
                update()
            }
            addSource(b) {
                second = it
                update()
            }
        }
    }
}

fun <T> T.asLiveData(): LiveData<T> {
    return MutableLiveData<T>().apply {
        postValue(this@asLiveData)
    }
}

//val x = Transformations.map(database.tracksDao().getTracks()) {
//    return@map it.isNotEmpty()
//}
//return MediatorLiveData<Boolean>().apply {
//    var value: Boolean? = null
//
//    fun update() {
//        if (value != null) {
//            removeSource(x)
//            postValue(value)
//        }
//    }
//
//    addSource(x) {
//        value = it
//        update()
//    }
//}