package dev.tiagoaguiar.kingburguer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.tiagoaguiar.kingburguer.api.KingBurguerService
import dev.tiagoaguiar.kingburguer.compose.home.CategoryUiState
import dev.tiagoaguiar.kingburguer.compose.home.HighlightUiState
import dev.tiagoaguiar.kingburguer.compose.home.HomeUiState
import dev.tiagoaguiar.kingburguer.data.ApiResult
import dev.tiagoaguiar.kingburguer.data.KingBurguerLocalStorage
import dev.tiagoaguiar.kingburguer.data.KingBurguerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val respotory: KingBurguerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchCategories()
        fetchHighlight()
    }

    fun fetchCategories() {
        _uiState.update { it.copy(categoryUiState = CategoryUiState(isLoading = true)) }
        viewModelScope.launch {
            val response = respotory.fetchFeed()
            when (response) {
                is ApiResult.Error -> {
                    val state = CategoryUiState(isLoading = false, error = response.message)
                    _uiState.update { it.copy(categoryUiState = state) }
                }

                is ApiResult.Success -> {
                    val state =
                        CategoryUiState(isLoading = false, categories = response.data.categories)
                    _uiState.update { it.copy(categoryUiState = state) }
                }
            }
        }
    }

    fun fetchHighlight() {
        _uiState.update { it.copy(highlightUiState = HighlightUiState(isLoading = true)) }
        viewModelScope.launch {
            val response = respotory.fetchHighlight()
            when (response) {
                is ApiResult.Error -> {
                    val state = HighlightUiState(isLoading = false, error = response.message)
                    _uiState.update { it.copy(highlightUiState = state) }
                }

                is ApiResult.Success -> {
                    val state = HighlightUiState(isLoading = false, product = response.data)
                    _uiState.update { it.copy(highlightUiState = state) }
                }
            }
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY]!!.applicationContext
                val service = KingBurguerService.create()
                val localStorage = KingBurguerLocalStorage(application)
                val respotory = KingBurguerRepository(service, localStorage)
                HomeViewModel(respotory)
            }
        }
    }
}