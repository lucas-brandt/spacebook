package com.example.spacebook.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.*
import java.time.Instant

interface SpacebookApi {
    @JsonClass(generateAdapter = true)
    data class ApiError(val type: String)

    @JsonClass(generateAdapter = true)
    data class ApiResponse<T>(
        val status: String,
        val data: T?,
        val error: ApiError?
    )

    @JsonClass(generateAdapter = true)
    data class User(
        @Json(name = "id") val id: Int,
        @Json(name = "email") val email: String,
        @Json(name = "name") val name: String,
        @Json(name = "registeredAt") val registeredAt: Instant,
        @Json(name = "githubUsername") val githubUsername: String?,
        @Json(name = "rating") val rating: Double?,
    )

    @JsonClass(generateAdapter = true)
    data class ActivityPost(
        @Json(name = "id") val id: Int,
        @Json(name = "userId") val userId: Int,
        @Json(name = "occurredAt") val occurredAt: Instant,
        @Json(name = "type") override val type: Type,
        @Json(name = "data") val data: Post
    ): Feed

    @JsonClass(generateAdapter = true)
    data class ActivityComment(
        @Json(name = "id") val id: Int,
        @Json(name = "userId") val userId: Int,
        @Json(name = "occurredAt") val occurredAt: Instant,
        @Json(name = "type") override val type: Type,
        @Json(name = "data") val data: Comment
    ): Feed

    @JsonClass(generateAdapter = true)
    data class ActivityGithub(
        @Json(name = "id") val id: Int,
        @Json(name = "userId") val userId: Int,
        @Json(name = "occurredAt") val occurredAt: Instant,
        @Json(name = "type") override val type: Type,
        @Json(name = "data") val data: GitHubEvent
    ): Feed

    @JsonClass(generateAdapter = true)
    data class Post(
        @Json(name = "id") val id: Int,
        @Json(name = "title") val title: String,
        @Json(name = "body") val body: String,
        @Json(name = "postedAt") val postedAt: Instant,
        @Json(name = "author") val author: User
    )

    @JsonClass(generateAdapter = true)
    data class Comment(
        @Json(name = "id") val id: Int,
        @Json(name = "message") val message: String,
        @Json(name = "userId") val userId: Int,
        @Json(name = "postId") val postId: Int,
        @Json(name = "commentedAt") val commentedAt: Instant
    )

    @JsonClass(generateAdapter = true)
    data class GitHubEvent(
        @Json(name = "githubId") val githubId: Int,
        @Json(name = "url") val url: String,
        @Json(name = "branch") val branch: String,
        @Json(name = "pullRequestNumber") val pullRequestNumber: Int,
    )

    object HighRating: Feed {
        override val type = Type.HIGH_RATING
    }

    enum class Type { NEW_POST, NEW_COMMENT, HIGH_RATING, GITHUB_EVENT, GITHUB_NEW_REPO, GITHUB_NEW_PR, GITHUB_MERGED_PR, GITHUB_PUSH }

    sealed interface Feed {
        val type: Type
    }

    @JsonClass(generateAdapter = true)
    data class SessionRequest(val email: String, val password: String)

    @POST("session")
    suspend fun login(@Body request: SessionRequest): ApiResponse<User>

    @GET("users/{id}/feed")
    suspend fun getFeed(@Path("id") id: Int): ApiResponse<List<Feed>>
}
