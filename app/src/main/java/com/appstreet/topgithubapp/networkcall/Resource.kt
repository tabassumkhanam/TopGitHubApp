package com.appstreet.topgithubapp.networkcall

sealed class Resource<T> {
    data class Loading<T>(val data: T?) : Resource<T>()
    data class Success<T>(val data: T?) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    data class InternetError<T>(val resId: Int) : Resource<T>()
}