package dev.tiagoaguiar.kingburguer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.tiagoaguiar.kingburguer.compose.KingBurguerApp
import dev.tiagoaguiar.kingburguer.compose.Screen
import dev.tiagoaguiar.kingburguer.ui.theme.KingBurguerTheme
import dev.tiagoaguiar.kingburguer.viewmodels.SplashViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SplashViewModel by viewModels { SplashViewModel.factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.hasSession.value == null
            }
        }
        setContent {
            KingBurguerTheme(dynamicColor = false) {

                val startState = viewModel.hasSession.collectAsState(null)
                startState.value?.let { logged ->
                    val startDestination = if (logged) Screen.MAIN else Screen.LOGIN

                    KingBurguerApp(startDestination)
                }
            }
        }
    }
}
