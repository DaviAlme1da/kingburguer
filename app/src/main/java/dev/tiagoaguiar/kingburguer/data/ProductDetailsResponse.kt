package dev.tiagoaguiar.kingburguer.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ProductDetailsResponse(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("picture_url")
    val pictureUrl: String,
    val price: Double,
    @SerializedName("created_date")
    val createdDate: Date,
    val categoryResponse: CategoryDetailResponse
)

data class CategoryDetailResponse(
    val id: Int,
    val name: String
)
