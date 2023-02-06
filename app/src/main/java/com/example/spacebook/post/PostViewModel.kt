package com.example.spacebook.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacebook.api.SpacebookApi
import com.example.spacebook.api.SpacebookApi.*
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PostViewModel(private val api: SpacebookApi) : ViewModel() {

    sealed class CommentState {
        object Retrieving : CommentState()
        data class Success(val result: List<Comment>) : CommentState()
        data class Error(val e: String) : CommentState()
    }

    sealed class PostState {
        object Retrieving : PostState()
        data class Success(val result: Post) : PostState()
        data class Error(val e: String) : PostState()
    }

    val feed: MutableLiveData<Feed> = MutableLiveData()
    var isPost = false

    private val _postState: MutableLiveData<PostState> = MutableLiveData(PostState.Retrieving)
    val postState: LiveData<PostState> get() = _postState

    private val _commentState: MutableLiveData<CommentState> = MutableLiveData(CommentState.Retrieving)
    val commentState: LiveData<CommentState> get() = _commentState

    fun getPostId(): Int {
        when(feed.value) {
            is ActivityPost -> {
                isPost = true
                return (feed.value as ActivityPost).data.id
            }
            is ActivityComment -> {
                //need to make post and comments network call
                return (feed.value as ActivityComment).data.postId
            }
            else -> { return 0 }
        }
    }

    fun getPost(postId: Int) {
        viewModelScope.launch {
            try {
                val res = api.getPost(postId)
                _postState.value = when {
                    res.data != null -> PostState.Success(res.data)
                    else -> PostState.Error("error")
                }
            } catch (e: HttpException) {
                _postState.value = PostState.Error(e.toString())
            }
        }
    }

    fun getComments(postId: Int) {
        viewModelScope.launch {
            try {
                val res = api.getPostComments(postId)
                _commentState.value = when {
                    res.data != null -> CommentState.Success(res.data)
                    else -> CommentState.Error("error")
                }
            } catch (e: HttpException) {
                _commentState.value = CommentState.Error(e.toString())
            }
        }
    }
}
