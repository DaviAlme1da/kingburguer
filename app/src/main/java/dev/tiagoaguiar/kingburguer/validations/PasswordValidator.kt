package dev.tiagoaguiar.kingburguer.validations

import dev.tiagoaguiar.kingburguer.R

class PasswordValidator {

    fun validate(password: String, confirmPassword: String): TextString?{

        if (password.isBlank()) {
            return ResourceString(R.string.error_password_blank)
        }

        if (password.length < 8) {
            return ResourceString(R.string.error_password_length)
        }

        if(confirmPassword.isNotBlank() && confirmPassword != password){
            return ResourceString(R.string.error_passwordConfirm_equal)
        }

        return null
    }

    fun validate(password: String): TextString?{

        if (password.isBlank()) {
            return ResourceString(R.string.error_password_blank)
        }

        if (password.length < 8) {
            return ResourceString(R.string.error_password_length)
        }

        return null
    }
}