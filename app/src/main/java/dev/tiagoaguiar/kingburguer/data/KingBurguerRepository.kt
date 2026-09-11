package dev.tiagoaguiar.kingburguer.data

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
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

    suspend fun fetchFeed(): ApiResult<FeedResponse> {
        val userCredentials = localStorage.fetchInitialUserCredentials()
        val token = "${userCredentials.tokenType} ${userCredentials.accessToken}"
        return apiCall { service.fetchFeed(token) }
    }

    suspend fun fetchProductById(productId: Int): ApiResult<ProductDetailsResponse> {
        val userCredentials = localStorage.fetchInitialUserCredentials()
        val token = "${userCredentials.tokenType} ${userCredentials.accessToken}"
        return apiCall { service.fetchProductById(token, productId) }
    }

    suspend fun fetchHighlight(): ApiResult<HighlightProductResponse> {
        val userCredentials = localStorage.fetchInitialUserCredentials()
        val token = "${userCredentials.tokenType} ${userCredentials.accessToken}"
        return apiCall { service.fetchHighlight(token) }
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

    suspend fun fetchMe(): ApiResult<ProfileResponse> {
        val userCredentials = localStorage.fetchInitialUserCredentials()
        val token = "${userCredentials.tokenType} ${userCredentials.accessToken}"
        return apiCall { service.fetchMe(token) }
    }

    suspend fun createCoupon(productId: Int): ApiResult<CouponResponse> {
        val userCredentials = localStorage.fetchInitialUserCredentials()
        val token = "${userCredentials.tokenType} ${userCredentials.accessToken}"
        return apiCall { service.createCoupon(token, productId) }
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
                        try {
                            val errorAuth = Gson().fromJson(json, ErrorAuth::class.java)
                            ApiResult.Error(errorAuth.detail.message)
                        } catch (e: JsonSyntaxException) {
                            val error = Gson().fromJson(json, Error::class.java)
                            ApiResult.Error(error.detail)
                        }
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