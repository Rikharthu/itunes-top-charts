package com.example.rikharthu.itunestopcharts.data

data class Resource<out T> constructor(val status: Status,
                                  val data: T? = null,
                                  val message: String? = null) {
    companion object {
        fun <T> success(data: T) = Resource(Status.SUCCESS, data = data)

        fun <T> error(message: String?) = Resource<T>(Status.ERROR, message = message)

        fun <T> loading(data: T?) = Resource(Status.ERROR, data = data)
    }

    enum class Status {
        LOADING, SUCCESS, ERROR
    }
}