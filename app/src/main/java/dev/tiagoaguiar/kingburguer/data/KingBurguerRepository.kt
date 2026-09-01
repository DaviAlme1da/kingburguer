package dev.tiagoaguiar.kingburguer.data

import com.google.gson.Gson
import dev.tiagoaguiar.kingburguer.api.KingBurguerService
import retrofit2.Response

class KingBurguerRepository(
    private val service: KingBurguerService,
    private val localStorage: KingBurguerLocalStorage
) {
    // get
    suspend fun fetchInitialCredentials() = localStorage.fetchInitialUserCredentials()
    suspend fun postUser(userRequest: UserRequest): ApiResult<UserCreateResponse> {

        val result = apiCall { service.postUser(userRequest) }
        return result
    }

    suspend fun login(loginRequest: LoginRequest, keepLogged: Boolean): ApiResult<LoginResponse> {
        val result = apiCall {
            service.login(loginRequest)
        }
        if (result is ApiResult.Success<LoginResponse>) {
            if (keepLogged) {
                updateCredentials(result.data)
            }
        }

        return result;
    }

    suspend fun refreshToken(request: RefreshTokenRequest): ApiResult<LoginResponse> {

        val userCredentials = localStorage.fetchInitialUserCredentials()
        val token = "${userCredentials.tokenType} ${userCredentials.refreshToken}"
        val result = apiCall { service.refreshToken(request, token) }

        if (result is ApiResult.Success<LoginResponse>) {
            updateCredentials(result.data)
        }
        return result
    }

    suspend fun updateCredentials(data: LoginResponse) {
        val newUserCredentials = UserCredentials(
            accessToken = data.accessToken,
            refreshToken = data.refreshToken,
            expiresTimestamp = data.expiresSeconds.toLong(),
            tokenType = data.tokenType
        )
        localStorage.updateUserCredential(newUserCredentials)
    }

    private suspend fun <T> apiCall(
        call: suspend () -> Response<T>
    ): ApiResult<T> {
        try {
            val response = call();
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

            return ApiResult.Success(data)

        } catch (e: Exception) {
            return ApiResult.Error(e.message ?: "unexpected exception")
        }
    }
}