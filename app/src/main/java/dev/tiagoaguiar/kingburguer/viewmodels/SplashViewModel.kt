package dev.tiagoaguiar.kingburguer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.tiagoaguiar.kingburguer.api.KingBurguerService
import dev.tiagoaguiar.kingburguer.data.KingBurguerLocalStorage
import dev.tiagoaguiar.kingburguer.data.KingBurguerRepository
import dev.tiagoaguiar.kingburguer.data.LoginResponse.Success
import dev.tiagoaguiar.kingburguer.data.RefreshTokenRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val repository: KingBurguerRepository
) : ViewModel() {

    private val _hasSessionState = MutableStateFlow<Boolean?>(null)
    val hasSession: StateFlow<Boolean?> = _hasSessionState


    init {
        viewModelScope.launch {

            _hasSessionState.value = with(repository.fetchInitialCredentials()) {
                when {
                    accessToken.isEmpty() -> false
                    System.currentTimeMillis() < expiresTimestamp -> true
                    else -> {
                        val response = repository.refreshToken(RefreshTokenRequest(refreshToken))
                        when (response) {
                            is Success -> true
                            else -> false
                        }

                    }
                }
            }
        }
    }


    companion object {
        val factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY]!!.applicationContext
                val service = KingBurguerService.create()
                val localStorage = KingBurguerLocalStorage(application)
                val repository = KingBurguerRepository(service, localStorage)
                SplashViewModel(repository)
            }
        }
    }
}