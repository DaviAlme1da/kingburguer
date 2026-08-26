package dev.tiagoaguiar.kingburguer.validations

fun Mask(pattern: String,currentValue: String, newValue: String): String{

    val str = newValue.filter { it.isLetterOrDigit() }

    var result = ""
    var i = 0
    for(char in pattern){
        if(char != '#'){
            result += char

            if(currentValue > newValue && result.length >= newValue.length){
                result = result.dropLast(1)
            }
            continue
        }
        if(i >= str.length) break
        result += str[i]
        i += 1
    }

    return result
}