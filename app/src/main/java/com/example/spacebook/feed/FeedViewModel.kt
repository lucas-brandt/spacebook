package com.example.spacebook.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacebook.api.SpacebookApi
import com.example.spacebook.api.SpacebookApi.*
import com.example.spacebook.login.LoginViewModel.State.Error.Reason.*
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FeedViewModel(private val api: SpacebookApi) : ViewModel() {

    sealed class State {
        object Retrieving : State()
        data class Success(val result: List<Feed>) : State()
        data class Error(val e: String) : State()
    }

    private val _state: MutableLiveData<State> = MutableLiveData(State.Retrieving)
    val state: LiveData<State> get() = _state

    fun getFeed(userId: Int) {
        viewModelScope.launch {
            try {
                val res = api.getFeed(userId)
                _state.value = when {
                    res.data != null -> State.Success(res.data)
                    else -> State.Error("error")
                }
            } catch (e: HttpException) {
                _state.value = State.Error(e.toString())
            }
        }
    }
}
