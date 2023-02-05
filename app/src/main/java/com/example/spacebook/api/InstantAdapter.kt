package com.example.spacebook.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.ToJson
import java.time.DateTimeException
import java.time.Instant
import java.time.format.DateTimeFormatter

class InstantAdapter(private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT) {

    @Suppress("Unused")
    @ToJson
    fun toJson(instant: Instant): String = formatter.format(instant)

    @Suppress("Unused")
    @FromJson
    fun fromJson(value: String): Instant {
        return try {
            Instant.from(formatter.parse(value))
        } catch (e: DateTimeException) {
            throw JsonDataException("malformed date: $value", e)
        }
    }
}
