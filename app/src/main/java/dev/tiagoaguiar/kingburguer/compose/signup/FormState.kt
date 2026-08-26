package dev.tiagoaguiar.kingburguer.compose.signup

import dev.tiagoaguiar.kingburguer.validations.TextString

data class FieldState(
    var field: String = "",
    var error: TextString? = null,
    val isValid: Boolean = false
)

data class FormState(
    val email: FieldState = FieldState(),
    val name: FieldState = FieldState(),
    val password: FieldState = FieldState(),
    val confirmPassword: FieldState = FieldState(),
    val document: FieldState = FieldState(),
    val birthday: FieldState = FieldState(),
    val formIsValid: Boolean = false
)
