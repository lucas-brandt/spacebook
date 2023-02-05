package com.example.spacebook.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.spacebook.api.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class SplashViewModel(private val tokenManager: TokenManager) : ViewModel() {
    // This seems quite unnecessary, since the value is constant and reading it is synchronous.
    // But it gives us a natural place to create more complex logic later, for example checking
    // the token's validity with the API, etc.
    val isLoggedIn = flow {
        delay(1500) // show the splash screen for a little while
        emit(tokenManager.isLoggedIn())
    }.asLiveData(viewModelScope.coroutineContext)
}
