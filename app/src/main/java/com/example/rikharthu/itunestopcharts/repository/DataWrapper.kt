package com.example.rikharthu.itunestopcharts.repository

data class DataWrapper<T>(val data: T? = null, val error: Throwable? = null)