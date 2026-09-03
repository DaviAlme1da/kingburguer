package dev.tiagoaguiar.kingburguer.common

import java.text.DateFormat
import java.util.Date
import java.util.Locale

fun Date.formatted(): String {
    val df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
    return df.format(this)
}