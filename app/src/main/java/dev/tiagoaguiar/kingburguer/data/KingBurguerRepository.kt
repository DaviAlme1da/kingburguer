package dev.tiagoaguiar.kingburguer.data

import android.R.attr.data
import com.google.gson.Gson
import dev.tiagoaguiar.kingburguer.api.KingBurguerService

class KingBurguerRepository(
    private val service: KingBurguerService,
    private val localStorage: KingBurguerLocalStorage
) {

    // get
    suspend fun fetchInitialCredentials() = localStorage.fetchInitialUserCredentials()

    suspend fun postUser(userRequest: UserRequest): UserCreateResponse {
        val response = service.postUser(userRequest)
        try {

            if (!response.isSuccessful) {

                val errorData = response.errorBody()?.string()?.let { json ->
                    if (response.code() == 401) {
                        Gson().fromJson(json, UserCreateResponse.ErrorAuth::class.java)
                    } else {
                        Gson().fromJson(json, UserCreateResponse.Error::class.java)
                    }
                }

                return errorData ?: UserCreateResponse.Error("internal server error")

            }

            val data = response.body()?.string()?.let { json ->
                Gson().fromJson(json, UserCreateResponse.Success::class.java)
            }

            return data ?: UserCreateResponse.Error("unexpected response success")
        } catch (e: Exception) {
            return UserCreateResponse.Error(e.message ?: "unexpected exception")
        }

    }

    suspend fun login(loginRequest: LoginRequest, keepLogged: Boolean): LoginResponse {

        if (keepLogged) {
            val userCredentials = UserCredentials(
                accessToken = data.accessToken,
                refreshToken = data.refreshToken,
                expiresTimestamp = data.expiresSeconds.toLong(),
                tokenType = data.tokenType
            )
            localStorage.updateUserCredential(userCredentials)
        }
    }

    private suspend fun apiCall(): ApiResult<LoginResponse>{
         try {
           val response = service.login(loginRequest)
            if (!response.isSuccessful) {
                val errorData = response.errorBody()?.string()?.let { json ->
                    if (response.code() == 401) {
                       val errorAuth = Gson().fromJson(json, ErrorAuth::class.java)
                        ApiResult.Error(errorAuth.detail.message)
                    } else {
                        Gson().fromJson(json, ApiResult.Error::class.java)
                    }
                }

                return errorData ?: ApiResult.Error("internal server error") // depois
            }

            val data = response.body()

                if (data == null) return ApiResult.Error("unexpected response success")

            return ApiResult.Sucess(data)

        } catch (e: Exception) {
             ApiResult.Error(e.message ?: "unexpected exception")
        }
    }

    suspend fun refreshToken(request: RefreshTokenRequest): LoginResponse {
        return try {
            val userCredentials = localStorage.fetchInitialUserCredentials()
            val response = service.refreshToken(
                request,
                "${userCredentials.tokenType} ${userCredentials.refreshToken}"
            )
            if (!response.isSuccessful) {
                val errorData = response.errorBody()?.string()?.let { json ->
                    Gson().fromJson(json, LoginResponse.ErrorAuth::class.java)
                }

                return errorData ?: LoginResponse.Error("internal server error")
            }

            val data = response.body()?.string()?.let { json ->
                Gson().fromJson(json, LoginResponse.Success::class.java)
            }

            if (data == null) return LoginResponse.Error("unexpected response success")

                val newUserCredentials = UserCredentials(
                    accessToken = data.accessToken,
                    refreshToken = data.refreshToken,
                    expiresTimestamp = data.expiresSeconds.toLong(),
                    tokenType = data.tokenType
                )
                localStorage.updateUserCredential(newUserCredentials)



            data

        } catch (e: Exception) {
            LoginResponse.Error(e.message ?: "unexpected exception")
        }
    }
}