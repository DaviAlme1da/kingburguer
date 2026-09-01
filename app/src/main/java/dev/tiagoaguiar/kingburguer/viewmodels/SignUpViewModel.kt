package dev.tiagoaguiar.kingburguer.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.tiagoaguiar.kingburguer.api.KingBurguerService
import dev.tiagoaguiar.kingburguer.compose.login.LoginUiState
import dev.tiagoaguiar.kingburguer.compose.signup.FieldState
import dev.tiagoaguiar.kingburguer.compose.signup.FormState
import dev.tiagoaguiar.kingburguer.compose.signup.SignUpUiState
import dev.tiagoaguiar.kingburguer.data.ApiResult
import dev.tiagoaguiar.kingburguer.data.KingBurguerLocalStorage
import dev.tiagoaguiar.kingburguer.data.KingBurguerRepository
import dev.tiagoaguiar.kingburguer.data.UserCreateResponse
import dev.tiagoaguiar.kingburguer.data.UserRequest
import dev.tiagoaguiar.kingburguer.validations.BirthdayValidator
import dev.tiagoaguiar.kingburguer.validations.DocumentValidator
import dev.tiagoaguiar.kingburguer.validations.EmailValidator
import dev.tiagoaguiar.kingburguer.validations.Mask
import dev.tiagoaguiar.kingburguer.validations.NameValidator
import dev.tiagoaguiar.kingburguer.validations.PasswordConfirmValidator
import dev.tiagoaguiar.kingburguer.validations.PasswordValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignUpViewModel(
    private val repository: KingBurguerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()
    var formState by mutableStateOf(FormState())
    private val emailValidator by mutableStateOf(EmailValidator())
    private val nameValidator by mutableStateOf(NameValidator())
    private val passwordValidator by mutableStateOf(PasswordValidator())
    private val passwordConfirmValidator by mutableStateOf(PasswordConfirmValidator())
    private val documentValidator by mutableStateOf(DocumentValidator())
    private val birthdayValidator by mutableStateOf(BirthdayValidator())

    fun reset() {
        _uiState.update { SignUpUiState() }
    }

    fun updateEmail(newEmail: String) {
        val textString = emailValidator.validate(newEmail)

        formState = formState.copy(
            email = FieldState(field = newEmail, error = textString, isValid = textString == null)
        )
        updateButton()
    }

    fun updateName(newName: String) {
        val textString = nameValidator.validate(newName)

        formState = formState.copy(
            name = FieldState(field = newName, error = textString, isValid = textString == null)
        )
        updateButton()
    }

    fun updatePassword(newPassword: String) {
        var textString = passwordValidator.validate(newPassword, formState.confirmPassword.field)

        formState = formState.copy(
            password = FieldState(
                field = newPassword,
                error = textString,
                isValid = textString == null
            )
        )
        textString = passwordConfirmValidator.validate(formState.confirmPassword.field, newPassword)
        formState = formState.copy(
            confirmPassword = FieldState(
                field = formState.confirmPassword.field,
                error = textString,
                isValid = textString == null
            )
        )
        updateButton()
    }

    fun updatePasswordConfirm(confirmPassword: String) {
        var textString =
            passwordConfirmValidator.validate(confirmPassword, formState.password.field)

        formState = formState.copy(
            confirmPassword = FieldState(
                field = confirmPassword,
                error = textString,
                isValid = textString == null
            )
        )

        textString = passwordValidator.validate(formState.password.field, confirmPassword)

        formState = formState.copy(
            password = FieldState(
                field = formState.password.field,
                error = textString,
                isValid = textString == null
            )
        )
        updateButton()
    }

    fun updateDocument(newDocument: String) {

        val textString = documentValidator.validate(formState.document.field, newDocument)

        formState = formState.copy(
            document = FieldState(
                field = documentValidator.result,
                error = textString,
                isValid = textString == null
            )
        )
        updateButton()
    }

    fun updateBirthday(newBirthday: String) {
        val textString = birthdayValidator.validate(formState.birthday.field, newBirthday)

        formState = formState.copy(
            birthday = FieldState(
                field = birthdayValidator.result,
                error = textString,
                isValid = textString == null
            )
        )
        updateButton()
    }

    private fun updateButton() {
        val formIsValid = with(formState) {
            email.isValid &&
                    name.isValid &&
                    password.isValid &&
                    confirmPassword.isValid &&
                    document.isValid &&
                    birthday.isValid
        }

        formState = formState.copy(
            formIsValid = formIsValid
        )
    }

    fun send() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {

            with(formState) {
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .parse(birthday.field)

                val dateFormatted = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(date!!)

                val documentFormatted = document.field.filter { it.isDigit() }

                val userRequest = UserRequest(
                    name = name.field,
                    email = email.field,
                    password = password.field,
                    document = documentFormatted,
                    birthday = dateFormatted
                )

                val result = repository.postUser(userRequest)
                Log.i("Teste", "result: $result")

                when(result){
                    is ApiResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, goToLogin = true) }
                    }
                    is ApiResult.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = result.message) }
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
                SignUpViewModel(repository)
            }
        }
    }
}