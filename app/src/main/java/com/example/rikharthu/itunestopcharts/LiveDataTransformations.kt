package com.example.rikharthu.itunestopcharts

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData

object LiveDataTransformations {

    /**
     * Transforms given LiveData to unsubscribe from source after first non-null value
     * or passed criteria returns 'true'
     */
    fun <T> single(from: LiveData<T>, criteria: ((T?) -> Boolean)? = null): LiveData<T> {
        return MediatorLiveData<T>().apply {
            var value: T? = null

            fun update() {
                if ((criteria == null && value != null) ||
                        (criteria != null && criteria(value))) {
                    removeSource(from)
                    postValue(value)
                }
            }

            addSource(from) {
                value = it
                update()
            }
        }
    }
}