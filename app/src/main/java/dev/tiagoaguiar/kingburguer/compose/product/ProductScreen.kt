package dev.tiagoaguiar.kingburguer.compose.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import dev.tiagoaguiar.kingburguer.R
import dev.tiagoaguiar.kingburguer.common.currency
import dev.tiagoaguiar.kingburguer.compose.component.KingButton
import dev.tiagoaguiar.kingburguer.data.CategoryDetailResponse
import dev.tiagoaguiar.kingburguer.data.ProductDetailsResponse
import dev.tiagoaguiar.kingburguer.ui.theme.KingBurguerTheme
import dev.tiagoaguiar.kingburguer.viewmodels.ProductViewModel
import java.util.Date

@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel(factory = ProductViewModel.factory)
) {
    val state = viewModel.uiState.collectAsState().value
    ProductScreen(modifier, state)
}

@Composable
fun ProductScreen(modifier: Modifier = Modifier, state: ProductUiState) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.error != null -> {
                Text(state.error, color = MaterialTheme.colorScheme.primary)
            }

            state.productDetail != null -> {
                ProductScreen(modifier, state.productDetail)
            }
        }
    }
}


@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    product: ProductDetailsResponse
) {
    val scrollState = rememberScrollState()
    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(scrollState)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(230.dp),
                    model = product.pictureUrl,
                    placeholder = painterResource(R.drawable.logo_splash),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = product.name,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp),
                        text = product.price.currency(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surface,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 56.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,

                    text = product.description
                )
            }

            KingButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                text = stringResource(R.string.get_coupon)
            ) { }
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LightProductScreenPreview() {
    KingBurguerTheme(dynamicColor = false, darkTheme = false) {
        ProductScreen(
            product = ProductDetailsResponse(
                id = 1,
                name = "Nome",
                description = "aaaaaaaa",
                price = 21.00,
                createdDate = Date(),
                pictureUrl = "",
                categoryResponse = CategoryDetailResponse(
                    id = 1, name = "teste"
                )
            )
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DarkProductScreenPreview() {
    KingBurguerTheme(dynamicColor = false, darkTheme = true) {
        ProductScreen(
            product = ProductDetailsResponse(
                id = 1,
                name = "Nome",
                description = "aaaaaaaa",
                price = 21.00,
                createdDate = Date(),
                pictureUrl = "",
                categoryResponse = CategoryDetailResponse(
                    id = 1, name = "teste"
                )
            )
        )
    }
}

