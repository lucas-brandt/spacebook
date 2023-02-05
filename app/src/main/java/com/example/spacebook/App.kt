package com.example.spacebook

import android.app.Application
import com.example.spacebook.Dependencies

@Suppress("Unused")
class App : Application() {

    private lateinit var _dependencies: Dependencies

    val dependencies: Dependencies get() = _dependencies

    override fun onCreate() {
        super.onCreate()
        _dependencies = Dependencies(this)
    }
}
