package com.example.carboncertificates.lib.api

import com.example.carboncertificates.lib.models.Certificate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface NetworkService {

    @GET("tech-test/certificates?limit=5&page=1")
    @Headers("API-KEY: FIELDMARGIN-TECH-TEST")
    suspend fun getAllData(): Response<List<Certificate>>
}