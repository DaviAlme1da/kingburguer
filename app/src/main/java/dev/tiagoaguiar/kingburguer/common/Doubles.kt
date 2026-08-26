package dev.tiagoaguiar.kingburguer.common

import java.text.NumberFormat
import java.util.Locale

fun Double.currency(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return formatter.format(this    )
}