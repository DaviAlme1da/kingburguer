package dev.tiagoaguiar.kingburguer.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.tiagoaguiar.kingburguer.api.KingBurguerService
import dev.tiagoaguiar.kingburguer.compose.product.ProductUiState
import dev.tiagoaguiar.kingburguer.data.ApiResult
import dev.tiagoaguiar.kingburguer.data.KingBurguerLocalStorage
import dev.tiagoaguiar.kingburguer.data.KingBurguerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: KingBurguerRepository
) : ViewModel() {
    val productId: Int = savedStateHandle["productId"] ?: 0
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        start()
    }

    fun start() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val response = repository.fetchProductById(productId)
            when (response) {
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = response.message) }
                }

                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, productDetail = response.data) }
                }
            }
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val application = this[APPLICATION_KEY]!!.applicationContext
                val service = KingBurguerService.create()
                val localStorage = KingBurguerLocalStorage(application)
                val repository = KingBurguerRepository(service, localStorage)
                ProductViewModel(savedStateHandle, repository)
            }
        }
    }
}