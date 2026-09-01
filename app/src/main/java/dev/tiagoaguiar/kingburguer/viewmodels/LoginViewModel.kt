package dev.tiagoaguiar.kingburguer.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.tiagoaguiar.kingburguer.api.KingBurguerService
import dev.tiagoaguiar.kingburguer.compose.login.FormState
import dev.tiagoaguiar.kingburguer.compose.login.LoginUiState
import dev.tiagoaguiar.kingburguer.compose.signup.FieldState
import dev.tiagoaguiar.kingburguer.data.ApiResult
import dev.tiagoaguiar.kingburguer.data.KingBurguerLocalStorage
import dev.tiagoaguiar.kingburguer.data.KingBurguerRepository
import dev.tiagoaguiar.kingburguer.data.LoginRequest
import dev.tiagoaguiar.kingburguer.data.LoginResponse
import dev.tiagoaguiar.kingburguer.validations.EmailValidator
import dev.tiagoaguiar.kingburguer.validations.PasswordValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: KingBurguerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    var formState by mutableStateOf(FormState())
    private val emailValidator = EmailValidator()
    private val passwordValidator = PasswordValidator()

    fun reset() {
        _uiState.update { LoginUiState() }
    }

    fun updateEmail(newEmail: String) {
        val textString = emailValidator.validate(newEmail)

        formState = formState.copy(
            email = FieldState(field = newEmail, error = textString, isValid = textString == null)
        )
        updateButton()
    }

    fun updatePassword(newPassword: String) {
        val textString = passwordValidator.validate(newPassword)

        formState = formState.copy(
            password = FieldState(
                field = newPassword,
                error = textString,
                isValid = textString == null
            )
        )
        updateButton()
    }

    private fun updateButton() {
        val formIsValid = with(formState) {
            email.isValid && password.isValid
        }

        formState = formState.copy(
            formIsValid = formIsValid
        )
    }

    fun updateRememberMe(rememberMe: Boolean) {
        formState = formState.copy(
            rememberMe = rememberMe
        )
    }


    fun send() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            with(formState) {

                val loginRequest = LoginRequest(
                    username = email.field,
                    password = password.field
                )

                val result = repository.login(loginRequest, rememberMe)
                Log.i("Teste", "result: $result")

                when (result) {
                    is ApiResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, goToHome = true) }
                    }

                    is ApiResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }

                    }
                }

            }
        }
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY]!!.applicationContext
                val service = KingBurguerService.create()
                val localStorage = KingBurguerLocalStorage(application)
                val repository = KingBurguerRepository(service, localStorage)
                LoginViewModel(repository)
            }
        }
    }

}