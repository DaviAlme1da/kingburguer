package dev.tiagoaguiar.kingburguer.compose.login

import dev.tiagoaguiar.kingburguer.compose.signup.FieldState

data class FormState(
    val email: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val rememberMe: Boolean = false,
    val formIsValid: Boolean = false
)
