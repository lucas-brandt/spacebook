package com.example.spacebook

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spacebook.api.InstantAdapter
import com.example.spacebook.api.SpacebookApi
import com.example.spacebook.api.SpacebookApi.*
import com.example.spacebook.api.TokenManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

/**
 * This is a very MVP dependency injection mechanism. It uses lazy vars for singletons,
 * and could use getter props for temporary instances. This could be replaced by something
 * like Hilt.
 */
class Dependencies(private val applicationContext: Context) {
    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(PostTypeFactory)
            .add(InstantAdapter())
            .build()
    }

    val tokenManager: TokenManager by lazy {
        TokenManager(applicationContext)
    }

    val api: SpacebookApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi).withNullSerialization())
            .baseUrl("https://spacebook.dev.fixdapp.com/api/")
            .client(
                OkHttpClient().newBuilder()
                    .addInterceptor(Interceptor { chain ->
                        val builder = chain.request().newBuilder()
                        builder.header("Authorization", "Bearer eyJhbGciOiJub25lIn0.eyJqdGkiOiIxMDNhZWExNy1mM2UzLTRiMTItODFiMi01NDc4ZmQ1M2Q2YzgiLCJleHAiOjE1NTIwMTgyMzEsInVpZCI6MX0")
                        return@Interceptor chain.proceed(builder.build())
                    })
                    .build()
            )
            .build()
            .create()
    }

    val PostTypeFactory = PolymorphicJsonAdapterFactory.of(Feed::class.java, "type")
        .withSubtype(ActivityPost::class.java, Type.NEW_POST.name)
        .withSubtype(ActivityComment::class.java, Type.NEW_COMMENT.name)
        .withSubtype(ActivityGithubPr::class.java, Type.GITHUB_NEW_PR.name)
        .withSubtype(ActivityGithubMergedPr::class.java, Type.GITHUB_MERGED_PR.name)
        .withSubtype(ActivityGithubPush::class.java, Type.GITHUB_PUSH.name)
        .withSubtype(ActivityGithubRepo::class.java, Type.GITHUB_NEW_REPO.name)
        .withSubtype(ActivityHighRating::class.java, Type.HIGH_RATING.name)
        .withDefaultValue(Default)
}

val Activity.dependencies: Dependencies get() = (application as App).dependencies
val Fragment.dependencies: Dependencies get() = requireActivity().dependencies

/**
 * This is a helper that allows controllers to construct ViewModels from Dependencies.
 */
fun Fragment.fromDependencies(block: Dependencies.() -> ViewModel): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return block.invoke(dependencies) as T
        }
    }
}
