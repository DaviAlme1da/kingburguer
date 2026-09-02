package dev.tiagoaguiar.kingburguer.data

data class UserCreateResponse(
        val id: Int,
        val name: String,
        val email: String,
        val document: String,
        val birthday: String
)



