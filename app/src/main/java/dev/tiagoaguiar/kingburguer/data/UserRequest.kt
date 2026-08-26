package dev.tiagoaguiar.kingburguer.data

data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
    val document: String,
    val birthday: String
)
