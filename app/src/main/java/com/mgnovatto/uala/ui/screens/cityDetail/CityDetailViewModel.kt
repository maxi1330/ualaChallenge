package com.mgnovatto.uala.ui.screens.cityDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgnovatto.uala.data.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing and providing the city description.
 * It interacts with the [CityRepository] to fetch the description based on a city name and country code.
 * The description is exposed as a [StateFlow] for reactive UI updates.
 */
@HiltViewModel
class CityDetailViewModel @Inject constructor(
    private val repository: CityRepository
) : ViewModel() {

    private val _description = MutableStateFlow<String?>("")
    val description: StateFlow<String?> = _description

    /**
     * Fetches the description for the given city and updates the state.
     *
     * @param cityName The name of the city.
     * @param countryCode The ISO country code of the city.
     * @param loadingText The text to display while loading the description.
     * @param notFoundText The text to display if the description could not be found.
     *
     * This method is called from the UI layer (e.g., inside a LaunchedEffect)
     * to ensure the description is fetched only once when the screen appears.
     */
    fun fetchCityDescription(
        cityName: String,
        countryCode: String,
        loadingText: String,
        notFoundText: String
    ) {
        viewModelScope.launch {
            _description.value = loadingText
            val desc = repository.getCityDescription(cityName, countryCode)
            _description.value = desc ?: notFoundText
        }
    }
}