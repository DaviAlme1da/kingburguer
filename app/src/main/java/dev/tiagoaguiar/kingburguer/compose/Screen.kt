package dev.tiagoaguiar.kingburguer.compose

enum class Screen(val route: String) {
    LOGIN("login"),
    SIGNUP("signup"),
    MAIN("main"),

    // rotas da tela principal
    HOME("home"),
    COUPON("coupon"),
    PROFILE("profile"),
    PRODUCT("product"),
}