package dev.tiagoaguiar.kingburguer.compose.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.tiagoaguiar.kingburguer.R
import dev.tiagoaguiar.kingburguer.common.formatted
import dev.tiagoaguiar.kingburguer.data.ProfileResponse
import dev.tiagoaguiar.kingburguer.ui.theme.KingBurguerTheme
import dev.tiagoaguiar.kingburguer.validations.Mask
import dev.tiagoaguiar.kingburguer.viewmodels.ProfileViewModel
import java.util.Date

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.factory)
) {
    val state = viewModel.uiState.collectAsState().value
    ProfileScreen(modifier, state)
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, state: ProfileUiState) {

    when {
        state.isLoading -> {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }


        }

        state.error != null -> {
            Box(contentAlignment = Alignment.Center) {
                Text(state.error, color = MaterialTheme.colorScheme.primary)
            }
        }

        state.profile != null -> {
            Box(contentAlignment = Alignment.TopStart) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(start = 20.dp, end = 20.dp, top = 16.dp)
                ) {
                    with(state.profile) {

                        ProfileProperty(R.string.prop_id, id)
                        ProfileProperty(R.string.prop_name, name)
                        ProfileProperty(R.string.prop_email, email)
                        ProfileProperty(
                            R.string.prop_document,
                            Mask("###.###.###-##", "", document)
                        )
                        ProfileProperty(R.string.prop_birthday, birthday.formatted())
                    }
                }
            }
        }
    }

}

@Composable
private fun ProfileProperty(
    @StringRes key: Int,
    value: Any
) {
    Column() {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(key),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                value.toString(),
                color = MaterialTheme.colorScheme.inverseSurface
            )
        }
        Divider(
            modifier = Modifier.padding(vertical = 14.dp),
            thickness = 0.8.dp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LightProfileScreenPreview() {
    KingBurguerTheme(dynamicColor = false, darkTheme = false) {
        val state = ProfileUiState(
            profile = ProfileResponse(
                0, "UserA", "user@gmail.com", "111.222.333-11", Date()
            )
        )
        ProfileScreen(modifier = Modifier, state = state)
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DarkHProfileScreenPreview() {
    KingBurguerTheme(dynamicColor = false, darkTheme = true) {
        val state = ProfileUiState(
            profile = ProfileResponse(
                0, "UserA", "user@gmail.com", "111.222.333-11", Date()
            )
        )
        ProfileScreen(modifier = Modifier, state = state)
    }
}