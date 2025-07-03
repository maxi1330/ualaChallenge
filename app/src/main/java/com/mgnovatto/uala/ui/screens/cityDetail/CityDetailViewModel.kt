package com.mgnovatto.uala.ui.screens.cityDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgnovatto.uala.data.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityDetailViewModel @Inject constructor(
    private val repository: CityRepository
) : ViewModel() {

    private val _description = MutableStateFlow<String?>("Cargando descripción...")
    val description: StateFlow<String?> = _description

    fun fetchCityDescription(cityName: String, countryCode: String) {
        viewModelScope.launch {
            _description.value = "Cargando descripción..."
            val desc = repository.getCityDescription(cityName, countryCode)
            _description.value = desc ?: "No se encontró descripción para esta ciudad."
        }
    }
}