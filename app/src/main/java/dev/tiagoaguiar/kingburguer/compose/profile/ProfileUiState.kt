package dev.tiagoaguiar.kingburguer.compose.profile

import dev.tiagoaguiar.kingburguer.data.ProfileResponse

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: ProfileResponse? = null,
    val error: String? = null
)
