package dev.tiagoaguiar.kingburguer.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class HighlightProductResponse(
    val id: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("picture_url")
    val pictureUrl: String,
    val cratedDate: Date
)
