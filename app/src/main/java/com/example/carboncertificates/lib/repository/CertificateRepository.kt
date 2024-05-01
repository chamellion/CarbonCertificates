package com.example.carboncertificates.lib.repository

import com.example.carboncertificates.lib.api.NetworkService
import com.example.carboncertificates.lib.models.Certificate
import retrofit2.Response
import javax.inject.Inject

class CertificateRepository @Inject constructor(private val networkService: NetworkService) {

    suspend fun getAllCertificates(): Response<List<Certificate>>{
        return networkService.getAllData()
    }
}