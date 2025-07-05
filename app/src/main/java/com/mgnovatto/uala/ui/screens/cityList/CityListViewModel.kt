package com.mgnovatto.uala.ui.screens.cityList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mgnovatto.uala.data.repository.CityRepository
import com.mgnovatto.uala.di.IoDispatcher
import com.mgnovatto.uala.domain.model.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

/**
 * Represents the download status of the city data in the UI.
 *
 * This sealed class is used to indicate whether the app is currently
 * loading city data, has completed loading successfully, or encountered an error.
 */
sealed class DownloadState {
    data object Loading : DownloadState()
    data object Ready : DownloadState()
    data object Error : DownloadState()
}

/**
 * ViewModel for managing the UI state and interactions on the city list screen.
 *
 * This ViewModel handles:
 * - Downloading and preparing the initial list of cities.
 * - Filtering cities based on a search query and "favorites only" toggle.
 * - Providing a paginated flow of cities for display.
 * - Handling user interactions such as search input and favorite toggling.
 *
 * @property repository Provides access to local and remote city data.
 * @property dispatcher Coroutine dispatcher for performing IO-bound operations.
 */
@HiltViewModel
class CityListViewModel @Inject constructor(
    private val repository: CityRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
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

    /**
     * Retries the initial data preparation process by re-triggering the city download.
     */
    fun retry() {
        prepareInitialData()
    }


    /**
     * Initiates the download of city data from the repository and updates the download state.
     * This is called at initialization and on manual retry.
     */
    private fun prepareInitialData() {
        viewModelScope.launch(dispatcher) {
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

    /**
     * A Flow that emits paginated lists of cities based on current search input and favorite filter.
     *
     * This flow reacts to changes in the search text or "favorites only" toggle,
     * and updates the list accordingly using the repository's paging mechanism.
     *
     * - Uses [combine] to monitor both [_searchText] and [_favoritesOnly] state flows.
     * - Uses [flatMapLatest] to switch to a new paging source whenever the filter inputs change.
     * - Uses [cachedIn] to cache the paging data in the scope of the ViewModel.
     *
     * Note: [flatMapLatest] requires opt-in to [ExperimentalCoroutinesApi].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val cities: Flow<PagingData<City>> = combine(searchText, favoritesOnly) { text, favs ->
        Pair(text, favs)
    }.flatMapLatest { (query, filterFavs) ->
        repository.getPaginatedCities(query, filterFavs)
    }.cachedIn(viewModelScope)

    /**
     * Updates the current search query.
     *
     * @param text New search string to filter cities.
     */
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    /**
     * Updates the "favorites only" filter state.
     *
     * @param isEnabled If true, only favorite cities will be shown.
     */
    fun onFavoritesOnlyChange(isEnabled: Boolean) {
        _favoritesOnly.value = isEnabled
    }

    /**
     * Toggles the favorite status of a given city.
     * Updates the data source accordingly.
     *
     * @param city The city to toggle favorite status for.
     */
    fun onToggleFavorite(city: City) {
        viewModelScope.launch(dispatcher)  {
            repository.toggleFavorite(city)
        }
    }
}