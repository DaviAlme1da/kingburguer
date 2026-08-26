package dev.tiagoaguiar.kingburguer.data
data class UserCredentials(
    val accessToken: String,
    val refreshToken : String,
    val expiresTimestamp: Long,
    val tokenType: String
)
