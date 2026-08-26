package dev.tiagoaguiar.kingburguer.validations

import dev.tiagoaguiar.kingburguer.R
import dev.tiagoaguiar.kingburguer.compose.signup.FieldState

class NameValidator {

    fun validate(name: String): TextString?{

        if (name.isBlank()) {
            return ResourceString(R.string.error_name_blank)
        }

        if (name.length < 3) {
            return ResourceString(R.string.error_name_length)
        }

        return null
    }

}