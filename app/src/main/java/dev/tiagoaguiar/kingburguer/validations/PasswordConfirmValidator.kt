package dev.tiagoaguiar.kingburguer.validations

import dev.tiagoaguiar.kingburguer.R
import dev.tiagoaguiar.kingburguer.compose.signup.FormState

class PasswordConfirmValidator() {

    fun validate(passwordConfirm: String, password: String): TextString?{

        if (passwordConfirm.isBlank()) {
            return ResourceString(R.string.error_passwordConfirm_blank)
        }

        if (password.isNotBlank() && passwordConfirm != password) {

            return ResourceString(R.string.error_passwordConfirm_equal)
        }

        return null
    }
}