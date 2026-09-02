package dev.tiagoaguiar.kingburguer.compose.product

import dev.tiagoaguiar.kingburguer.data.ProductDetailsResponse

data class ProductUiState(
    val isLoading: Boolean = false,
    val productDetail: ProductDetailsResponse? = null,
    val error: String? = null
)
