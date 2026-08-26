package dev.tiagoaguiar.kingburguer.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.tiagoaguiar.kingburguer.compose.home.Product

class ProductViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val productId: Int = savedStateHandle["productId"] ?: 0
    val product: Product = Product(1, "")

    companion object{
        val factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                ProductViewModel(savedStateHandle)
            }
        }
    }
}