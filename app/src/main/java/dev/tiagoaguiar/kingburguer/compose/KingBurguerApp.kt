package dev.tiagoaguiar.kingburguer.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.tiagoaguiar.kingburguer.compose.login.LoginScreen
import dev.tiagoaguiar.kingburguer.compose.signup.SignUpScreen
import dev.tiagoaguiar.kingburguer.ui.theme.KingBurguerTheme
import dev.tiagoaguiar.kingburguer.viewmodels.SplashViewModel

@Composable
fun KingBurguerApp(
    starDestination: Screen
) {
    val navController = rememberNavController()
    KingBurguerNavHost(navController, starDestination)
}

@Composable
fun KingBurguerNavHost(
    navController: NavHostController,
    starDestination: Screen
) {
    NavHost(
        navController = navController,
        startDestination = starDestination.route
    ) {
        composable(Screen.LOGIN.route) {
            LoginScreen(
                onSignUpClick = {
                    navController.navigate(Screen.SIGNUP.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.MAIN.route) {
                        popUpTo(Screen.LOGIN.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.SIGNUP.route) {
            SignUpScreen(
                onNavigationClick = {
                    navController.navigateUp()
                },
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }
        composable(Screen.MAIN.route) {
            MainScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KingBurguerAppPreview() {
    KingBurguerTheme() {
        KingBurguerApp(Screen.LOGIN)
    }
}