package dev.tiagoaguiar.kingburguer.validations

import dev.tiagoaguiar.kingburguer.R
import dev.tiagoaguiar.kingburguer.compose.signup.FieldState
import dev.tiagoaguiar.kingburguer.validations.Mask
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BirthdayValidator {

    private val pattern = "##/##/####"
    lateinit var result: String

    fun validate(currentBirthday: String, birthday: String): TextString? {
        result = Mask(pattern, currentBirthday, birthday)

        if (birthday.isBlank()) {
            return ResourceString(R.string.error_birthday_blank)
        }

        if (birthday.length != pattern.length) {
            return ResourceString(R.string.error_birthday_invalid)
        }

        try {
            //val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            //sdf.isLenient = false
            //val dataFinal = sdf.parse(result)

            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).run {
                isLenient = false
                parse(result)
            }?.also {
                val now = Date()
                if (it.after(now)) {
                    return ResourceString(R.string.error_birthday_biggerToday)
                }
            }

        } catch (e: ParseException) {
            return ResourceString(R.string.error_birthday_invalid)
        }

        return null
    }
}