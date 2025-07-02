package com.mgnovatto.uala.ui.screens.cityList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgnovatto.uala.data.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val repository: CityRepository
) : ViewModel() {

    init {
        println("CityListViewModel LLEGO!")
        viewModelScope.launch {
            repository.downloadCities()
        }
    }
}