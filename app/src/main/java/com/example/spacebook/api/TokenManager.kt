package com.example.spacebook.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenManager(applicationContext: Context) : Interceptor {

    private val sharedPreferences =
        applicationContext.getSharedPreferences("Spacebook", Context.MODE_PRIVATE)

    private var token: String? = null

    init {
        token = sharedPreferences.getString("auth_token", null)
    }

    fun isLoggedIn(): Boolean {
        // TODO: should probably check if the token is still valid
        return token != null
    }

    fun logout() {
        setToken(null)
    }

    private fun setToken(token: String?) {
        synchronized(this) {
            this.token = token
            sharedPreferences.edit().apply {
                if (token != null) putString("auth_token", token) else remove("auth_token")
            }.apply()
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().withToken()
        val response = chain.proceed(request)
        val token = response.header("Authorization")?.removePrefix("Bearer ")
        if (token != null) setToken(token)
        return response
    }

    private fun Request.withToken(): Request {
        val token = token ?: return this
        return newBuilder().addHeader("Authorization", "Bearer $token").build()
    }
}
