package dev.tiagoaguiar.kingburguer.api

import com.google.gson.GsonBuilder
import dev.tiagoaguiar.kingburguer.BuildConfig
import dev.tiagoaguiar.kingburguer.data.CouponResponse
import dev.tiagoaguiar.kingburguer.data.FeedResponse
import dev.tiagoaguiar.kingburguer.data.LoginRequest
import dev.tiagoaguiar.kingburguer.data.LoginResponse
import dev.tiagoaguiar.kingburguer.data.ProductDetailsResponse
import dev.tiagoaguiar.kingburguer.data.ProfileResponse
import dev.tiagoaguiar.kingburguer.data.RefreshTokenRequest
import dev.tiagoaguiar.kingburguer.data.UserCreateResponse
import dev.tiagoaguiar.kingburguer.data.UserRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface KingBurguerService {

    @POST("users")
    suspend fun postUser(
        @Body userRequest: UserRequest,
        @Header("x-secret-key") secretKey: String = BuildConfig.X_SECRET_KEY,
    ): Response<UserCreateResponse>

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
        @Header("x-secret-key") secretKey: String = BuildConfig.X_SECRET_KEY,
    ): Response<LoginResponse>

    @PUT("auth/refresh-token")
    suspend fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest,
        @Header("Authorization") token: String,
    ): Response<LoginResponse>

    @GET("feed")
    suspend fun fetchFeed(
        @Header("Authorization") token: String,
    ): Response<FeedResponse>

    @GET("products/{id}")
    suspend fun fetchProductById(
        @Header("Authorization") token: String,
        @Path("id") productId: Int,
    ): Response<ProductDetailsResponse>

    @GET("users/me")
    suspend fun fetchMe(
        @Header("Authorization") token: String,
    ): Response<ProfileResponse>

    @POST("products/{id}/coupon")
    suspend fun createCoupon(
        @Header("Authorization") token: String,
        @Path("id") productId: Int,
    ): Response<CouponResponse>


    companion object {

        private const val BASE_URL = "https://hades.tiagoaguiar.co/kingburguer/"

        fun create(): KingBurguerService {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val clientOk = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientOk)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(KingBurguerService::class.java)
        }
    }
}