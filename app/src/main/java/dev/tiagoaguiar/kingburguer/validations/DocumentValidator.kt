package dev.tiagoaguiar.kingburguer.validations

import dev.tiagoaguiar.kingburguer.R
import dev.tiagoaguiar.kingburguer.compose.signup.FieldState

class DocumentValidator {

    private val pattern = "###.###.###-##"
    lateinit var result: String

    fun validate(currentDocument: String, document: String): TextString?{
        result = Mask(pattern, currentDocument, document)

        if (document.isBlank()) {
            return ResourceString(R.string.error_document_blank)
        }

        if (document.length != pattern.length) {
            return ResourceString(R.string.error_document_length)
        }

        return null
    }

}