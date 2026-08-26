package dev.tiagoaguiar.kingburguer.validations

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

interface TextString{

    @get:Composable
    val value: String

    /*@Composable
    fun asString(): String*/

}

class ResourceString(@StringRes private val input: Int): TextString{


    override val value: String
        @Composable
        get() = stringResource(input)

    /*@Composable
    override fun asString(): String {
        return stringResource(input)
    }*/
}

class RawString(private val input: String): TextString{

    override val value: String
        @Composable
        get() = input

    /*@Composable
    override fun asString(): String {
        return input
    }*/
}