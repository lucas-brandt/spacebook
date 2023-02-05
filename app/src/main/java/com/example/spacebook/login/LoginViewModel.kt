package com.example.spacebook.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacebook.api.SpacebookApi
import com.example.spacebook.api.SpacebookApi.User
import com.example.spacebook.login.LoginViewModel.State.Error.Reason.*
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val api: SpacebookApi) : ViewModel() {

    sealed class State {
        object FillingOutForm : State()
        object LoggingIn : State()
        data class Success(val result: User) : State()
        data class Error(val reason: Reason) : State() {
            enum class Reason { INVALID_EMAIL, INVALID_PASSWORD, INCORRECT_PASSWORD, NETWORK_ERROR }
        }
    }

    private val _state: MutableLiveData<State> = MutableLiveData(State.FillingOutForm)
    val state: LiveData<State> get() = _state

    fun login(email: String, password: String) {
        _state.value = State.LoggingIn
        if (!isValidEmail(email)) {
            _state.value = State.Error(INVALID_EMAIL)
            return
        }
        if (!isValidPassword(password)) {
            _state.value = State.Error(INVALID_PASSWORD)
            return
        }
        viewModelScope.launch {
            try {
                val res = api.login(SpacebookApi.SessionRequest(email, password))
                _state.value = when {
                    res.data != null -> State.Success(res.data)
                    res.error?.type == "NOT_AUTHENTICATED" -> State.Error(INCORRECT_PASSWORD)
                    else -> State.Error(NETWORK_ERROR)
                }
            } catch (e: HttpException) {
                // TODO: not the VM's responsibility to wrap retrofit
                if (e.response()?.code() == 401) {
                    _state.value = State.Error(INCORRECT_PASSWORD)
                } else {
                    _state.value = State.Error(NETWORK_ERROR)
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        // TODO: could create more complex validation
        return email.matches(Regex(".+@.+"))
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}
