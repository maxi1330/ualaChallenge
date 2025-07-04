package com.mgnovatto.uala.ui.screens.cityList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mgnovatto.uala.data.repository.CityRepository
import com.mgnovatto.uala.domain.model.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

sealed class DownloadState {
    data object Loading : DownloadState()
    data object Ready : DownloadState()
    data object Error : DownloadState()
}

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val repository: CityRepository
) : ViewModel() {

    private val _downloadState = MutableStateFlow<DownloadState>(DownloadState.Loading)
    val downloadState: StateFlow<DownloadState> = _downloadState

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _favoritesOnly = MutableStateFlow(false)
    val favoritesOnly: StateFlow<Boolean> = _favoritesOnly

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
                _downloadState.value = DownloadState.Error
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val cities: Flow<PagingData<City>> = combine(searchText, favoritesOnly) { text, favs ->
        Pair(text, favs)
    }.flatMapLatest { (query, filterFavs) ->
        repository.getPaginatedCities(query, filterFavs)
    }.cachedIn(viewModelScope)

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onFavoritesOnlyChange(isEnabled: Boolean) {
        _favoritesOnly.value = isEnabled
    }

    fun onToggleFavorite(city: City) {
        viewModelScope.launch {
            repository.toggleFavorite(city)
        }
    }
}