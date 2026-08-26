package dev.tiagoaguiar.kingburguer.validations

import android.util.Patterns
import dev.tiagoaguiar.kingburguer.R

class EmailValidator {

    fun validate(email: String): TextString?{
        if (email.isBlank()) {
            return ResourceString(R.string.error_email_blank)
        }
        if (!isEmailValid(email)) {
            return ResourceString(R.string.error_email_invalid)
        }

        return null
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}