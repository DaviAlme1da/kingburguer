package dev.tiagoaguiar.kingburguer.compose.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.tiagoaguiar.kingburguer.R
import dev.tiagoaguiar.kingburguer.compose.component.KingAlert
import dev.tiagoaguiar.kingburguer.compose.component.KingButton
import dev.tiagoaguiar.kingburguer.compose.component.KingTextField
import dev.tiagoaguiar.kingburguer.compose.component.KingTextTitle
import dev.tiagoaguiar.kingburguer.ui.theme.KingBurguerTheme
import dev.tiagoaguiar.kingburguer.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    onSignUpClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModel.factory)
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val scrollState = rememberScrollState()
        var passwordHidden by remember { mutableStateOf(true) }

        val uiState by viewModel.uiState.collectAsState()

        Column {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollState)
            ) {
                LaunchedEffect(key1 = uiState.goToHome) {
                    if (uiState.goToHome) {
                        onNavigateToHome()
                        viewModel.reset()
                    }
                }

                uiState.error?.let { message ->
                    KingAlert(
                    onDismissRequest = {},
                        onConfirmation = {
                            viewModel.reset()
                        },
                        dialogTitle = stringResource(id = R.string.app_name),
                        dialogText = message,
                        icon = Icons.Filled.Info
                    )

                }


                KingTextTitle(text = stringResource(id = R.string.login))

                KingTextField(
                    value = viewModel.formState.email.field,
                    label = R.string.email,
                    placeholder = R.string.hint_email,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    error = viewModel.formState.email.error?.value
                ) {
                    viewModel.updateEmail(it)
                }

                KingTextField(
                    value = viewModel.formState.password.field,
                    label = R.string.password,
                    placeholder = R.string.hint_password,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    error = viewModel.formState.password.error?.value,
                    obfuscate = passwordHidden,
                    trailingIcon = {
                        IconButton(onClick = { passwordHidden = !passwordHidden }) {
                            val image = if (passwordHidden) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            }
                            val description = if (passwordHidden) {
                                stringResource(id = R.string.show_password)
                            } else {
                                stringResource(id = R.string.hide_password)
                            }
                            Icon(
                                imageVector = image,
                                contentDescription = description
                            )
                        }
                    }
                ) {
                    viewModel.updatePassword(it)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = viewModel.formState.rememberMe,
                        onCheckedChange = {
                            viewModel.updateRememberMe(it)
                        }
                    )
                    Text(stringResource(id = R.string.remember_me))
                }

                KingButton(
                    text = stringResource(id = R.string.send),
                    enabled = viewModel.formState.formIsValid,
                    loading = uiState.isLoading
                ) {
                    viewModel.send()
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.have_account))
                    TextButton(onClick = onSignUpClick) {
                        Text(stringResource(id = R.string.sign_up))
                    }
                }
            }

            Image(
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.BottomCenter,
                painter = painterResource(id = R.drawable.cover3),
                contentDescription = stringResource(id = R.string.hambuerguer)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LightLoginScreenPreview() {
    KingBurguerTheme(dynamicColor = false, darkTheme = false) {
        LoginScreen({}, onNavigateToHome = {})
    }
}


@Preview(showBackground = true)
@Composable
fun DarkLoginScreenPreview() {
    KingBurguerTheme(dynamicColor = false, darkTheme = true) {
        LoginScreen({}, {})
    }
}