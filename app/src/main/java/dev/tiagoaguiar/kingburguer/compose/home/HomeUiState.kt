package dev.tiagoaguiar.kingburguer.compose.home

import dev.tiagoaguiar.kingburguer.data.CategoryResponse

data class HomeUiState(
    val isLoading: Boolean = false,
    val categories: List<CategoryResponse> = emptyList(),
    val error: String? = null,
)