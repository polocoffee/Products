package com.banklannister.technologyshop.data

sealed class State<T>(
    val data: T? = null,
    val message: String? = null,
) {

    class Success<T>(data: T?) : State<T>(data)
    class Error<T>(data: T? = null, message: String) : State<T>(data, message)
}
