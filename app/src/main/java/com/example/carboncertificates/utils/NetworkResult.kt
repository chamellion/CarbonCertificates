package com.example.carboncertificates.utils

sealed interface NetworkResult{
    data object NoInternet: NetworkResult
    data object Loading: NetworkResult
    data class Error(val error: String?): NetworkResult
    data class Success<T>(val data: T): NetworkResult
}