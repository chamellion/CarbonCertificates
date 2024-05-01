package com.example.carboncertificates.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carboncertificates.lib.repository.CertificateRepository
import com.example.carboncertificates.utils.NetworkResult
import com.example.carboncertificates.utils.isOnline
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CertificatesViewModel"

@HiltViewModel
class CertificatesViewModel @Inject constructor(
    private val repository: CertificateRepository,
    application: Application
) : AndroidViewModel(application) {


    private val _viewState: MutableStateFlow<NetworkResult> =
        MutableStateFlow(NetworkResult.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            if (application.isOnline()){
                try {
                    val response = repository.getAllCertificates()
                    if (response.isSuccessful) {
                        val body = response.body()
                        _viewState.value = NetworkResult.Success(body)
                    } else {
                        _viewState.value = NetworkResult.Error("Error retrieving Data")
                    }
                } catch (exception: Exception) {
                    _viewState.value = NetworkResult.Error(exception.message)
                    Log.e(TAG, exception.message ?: "No message printed")
                }
            }else{
                _viewState.value = NetworkResult.NoInternet
            }
        }
    }

}