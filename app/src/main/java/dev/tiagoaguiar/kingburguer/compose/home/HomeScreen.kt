package dev.tiagoaguiar.kingburguer.compose.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tiagoaguiar.kingburguer.R
import dev.tiagoaguiar.kingburguer.common.currency
import dev.tiagoaguiar.kingburguer.ui.theme.KingBurguerTheme
import dev.tiagoaguiar.kingburguer.ui.theme.Orange600

data class Product(
    val id: Int,
    val name: String,
    @DrawableRes val picture: Int = R.drawable.example,
    val price: Double = 19.9
)

data class Category(
    val name: String,
    val products: List<Product>
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onProductClicked: (Int) -> Unit
) {
    val categories = listOf(
        Category(
            name = "Vegetariano",
            products = listOf(
                Product(1,"Combo v1"),
                Product(2,"Combo v2"),
                Product(3,"Combo v3"),
            )
        ),
        Category(
            name = "Bovino",
            products = listOf(
                Product(4,"Combo b1 com nome muito grande para testar o app"),
                Product(5,"Combo b2"),
                Product(6,"Combo b3"),
            )
        ),
        Category(
            name = "Sobremessa",
            products = listOf(
                Product(7,"Combo s1"),
                Product(8,"Combo s2"),
                Product(9,"Combo s3"),
            )
        ),
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(Color.Blue),
                painter = painterResource(R.drawable.highlight),
                contentDescription = null,
                contentScale = ContentScale.Crop

            )
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .padding(bottom = 12.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 6.dp
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Orange600
                )
            ) {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.get_coupon)
                )
            }
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            itemsIndexed(categories) { index, cat ->
                val topPadding = if (index == 0) 20.dp else 0.dp
                val bottomPadding = if (index == categories.size - 1) 20.dp else 0.dp
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp, bottom = 12.dp, top = topPadding),
                    text = cat.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineMedium
                )
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    LazyRow(
                        modifier = Modifier
                            .padding(bottom = bottomPadding)
                    ) {
                        itemsIndexed(cat.products) { index, product ->
                            val startPadding = if (index == 0) 20.dp else 8.dp
                            val endPadding = if (index == categories.size - 1) 20.dp else 8.dp

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .widthIn(max = 160.dp)
                                    .padding(start = startPadding, end = endPadding)
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(140.dp, 180.dp)
                                        .border(
                                            BorderStroke(0.3.dp, Color.Gray),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            onProductClicked(product.id)
                                        },
                                    painter = painterResource(product.picture),
                                    contentDescription = product.name,
                                )
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.inverseSurface,
                                    text = product.name,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            RoundedCornerShape(4.dp)
                                        ),
                                    color = MaterialTheme.colorScheme.surface,
                                    text = product.price.currency(),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium
                                )


                            }
                        }
                    }
                }


            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LightHomeScreenPreview() {
    KingBurguerTheme(dynamicColor = false, darkTheme = false) {
        HomeScreen(){}
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DarkHomeScreenPreview() {
    KingBurguerTheme(dynamicColor = false, darkTheme = true) {
        HomeScreen(){}
    }
}