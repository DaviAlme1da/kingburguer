package dev.tiagoaguiar.kingburguer.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class FeedResponse(
    val categories: List<CategoryResponse>
)

data class CategoryResponse(
    val id: Int,
    val name: String,
    val products: List<ProductResponse>
)
data class ProductResponse(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("picture_url")
    val pictureUrl: String,
    val price: Double,
    @SerializedName("created_date")
    val createdDate: Date
)