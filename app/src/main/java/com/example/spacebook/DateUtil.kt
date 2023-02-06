package com.example.spacebook

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class DateUtil {
    companion object {
        fun formatInstant(instant: Instant): String {
            return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                .withLocale(Locale.US)
                .withZone(ZoneId.systemDefault())
                .format(instant)
        }
    }
}