package dev.tiagoaguiar.kingburguer.compose.coupon

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tiagoaguiar.kingburguer.ui.theme.KingBurguerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class Coupon(
    val id: Int,
    val productId: Int,
    val coupon: String,
    val createdAt: String,
    val expirationAt: String,
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CouponScreen(modifier: Modifier = Modifier) {
    val coupons = listOf(
        Coupon(
            id = 510,
            productId = 1,
            coupon = "FHMQGA",
            createdAt = "2026-09-02T12:24:19",
            expirationAt = "2026-09-02T12:24:19"
        ),
        Coupon(
            id = 526,
            productId = 1,
            coupon = "CFOFTX",
            createdAt = "2026-05-06T15:47:43",
            expirationAt = "2026-05-21T15:47:44"
        ),
        Coupon(
            id = 527,
            productId = 4,
            coupon = "SKATKD",
            createdAt = "2026-05-06T16:57:02",
            expirationAt = "2025-12-12T16:57:02"
        ),
        Coupon(
            id = 528,
            productId = 4,
            coupon = "RCIBWL",
            createdAt = "2026-05-06T16:59:14",
            expirationAt = "2026-12-12T16:57:02"
        ),
        Coupon(
            id = 529,
            productId = 4,
            coupon = "ABMANS",
            createdAt = "2026-05-06T17:03:05",
            expirationAt = "2026-05-21T17:03:05"
        ),
        Coupon(
            id = 530,
            productId = 4,
            coupon = "YDAZSD",
            createdAt = "2025-05-06T17:05:32",
            expirationAt = "2025-05-21T17:05:32"
        ),
        Coupon(
            id = 531,
            productId = 4,
            coupon = "IULAWA",
            createdAt = "2025-05-06T17:06:33",
            expirationAt = "2025-05-21T17:06:33"
        ),
        Coupon(
            id = 532,
            productId = 4,
            coupon = "NLPXRO",
            createdAt = "2025-05-06T17:07:55",
            expirationAt = "2025-05-21T17:07:55"
        ),
        Coupon(
            id = 533,
            productId = 6,
            coupon = "ZBVEAH",
            createdAt = "2025-05-06T17:28:46",
            expirationAt = "2025-05-21T17:28:47"
        ),
        Coupon(
            id = 534,
            productId = 10,
            coupon = "YJNSTM",
            createdAt = "2025-05-06T17:30:33",
            expirationAt = "2025-05-21T17:30:33"
        ),
        Coupon(
            id = 535,
            productId = 4,
            coupon = "ZBNPPB",
            createdAt = "2025-05-06T18:28:22",
            expirationAt = "2025-05-21T18:28:22"
        ),
        Coupon(
            id = 536,
            productId = 4,
            coupon = "EMVAOQ",
            createdAt = "2025-05-06T19:00:18",
            expirationAt = "2025-05-21T19:00:18"
        ),
    )


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        val options = listOf("Ativos", "Expirados", "Todos")
        var selectedOption by remember { mutableStateOf(options.first()) }

        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.forEach { text ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = { selectedOption = text }
                    )
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(start = 0.dp)
                    )
                }
            }
        }

        LazyColumn {
            itemsIndexed(coupons) { index, item ->
                val expired = toDate(item.expirationAt).before(Date())
                if (selectedOption == options[0] && expired) return@itemsIndexed
                if (selectedOption == options[1] && !expired) return@itemsIndexed

                val topPadding = if (index == 0) 12.dp else 20.dp
                val bottomPadding = if (index == coupons.size - 1) 12.dp else 0.dp
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            end = 20.dp,
                            bottom = bottomPadding,
                            top = topPadding
                        ),
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
                    ) {
                        Text(
                            text = item.coupon,
                            color = if (expired) Color.Gray else MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )

                        Text(
                            text = "Expira: ${toDateBR(item.expirationAt)}",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
    }

}

private fun toDate(value: String): Date {
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        .parse(value)
    return date ?: Date()
}

private fun toDateBR(value: String): String {
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        .parse(value)

    val dateFormatted = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        .format(date!!) // unwrap

    return dateFormatted
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LightCouponScreenPreview() {
    KingBurguerTheme(dynamicColor = false, darkTheme = false) {
        CouponScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DarkCouponScreenPreview() {
    KingBurguerTheme(dynamicColor = false, darkTheme = true) {
        CouponScreen()
    }
}