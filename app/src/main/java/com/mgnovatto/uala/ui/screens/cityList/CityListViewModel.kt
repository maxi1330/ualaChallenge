package com.mgnovatto.uala.ui.screens.cityList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgnovatto.uala.data.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

sealed class DownloadState {
    data object Loading : DownloadState()
    data object Ready : DownloadState()
    data class Error(val message: String) : DownloadState()
}

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val repository: CityRepository
) : ViewModel() {

    private val _downloadState = MutableStateFlow<DownloadState>(DownloadState.Loading)
    val downloadState: StateFlow<DownloadState> = _downloadState

    init {
        prepareInitialData()
    }

    fun retry() {
        prepareInitialData()
    }


    private fun prepareInitialData() {
        viewModelScope.launch {
            _downloadState.value = DownloadState.Loading

            val success = withTimeoutOrNull(60000L) {
                repository.downloadCities()
            }

            if (success == true) {
                _downloadState.value = DownloadState.Ready
            } else {
                _downloadState.value = DownloadState.Error("Falló la preparación de datos.")
            }
        }
    }
}