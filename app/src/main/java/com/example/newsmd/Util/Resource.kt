package com.example.newsmd.Util

sealed class Resource<T>(val data:T? = null, val message:String? = null ) //null is the default value
{
    class Success<T>(data: T?):Resource<T>(data = data)
    class Error<T>(message: String?):Resource<T>(null, message)
}