package com.appstreet.topgithubapp.utilities

import androidx.annotation.NonNull
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AppUtils {

    companion object {

        val DISPLAY: SimpleDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        val DISPLAY_SHORT: SimpleDateFormat = SimpleDateFormat("EEE", Locale.getDefault())

        fun getCurrentDay(): String {
            try {
                val date = Date()
                return DISPLAY_SHORT.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        fun epocSecondsToDate(epocSeconds: Long): Date {
            val c = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            c.timeInMillis = epocSeconds * 1000
            return c.time
        }

        fun dateToDayDateString(
            date: Date,
            useShortFormat: Boolean
        ): String {
            return if (useShortFormat) {
                DISPLAY_SHORT.format(date).toUpperCase()
            } else {
                DISPLAY.format(date).toUpperCase()
            }
        }

        /**
         * Method to get the long time in milli seconds from the given date. The format of the data should be in m/d/YYYY
         *
         * @param entry The format of the data should be in m/d/YYYY e.g 4/6/2017
         * @return long date
         */
        fun getTimeinMillis(@NonNull entry: String): Long {
            var millis: Long = -1
            if (entry.isNotEmpty()) {
                try {
                    try {
                        val formatter =
                            SimpleDateFormat("dd/MM/yyyy", Locale("en"))
                        val date = formatter.parse(entry)
                        return date.getTime()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    if (!entry.startsWith("/Date")) {
                        val formatter =
                            SimpleDateFormat(
                                "M/d/yyyy",
                                Locale("en")
                            ); // I assume d-M, you may refer to M-d for month-day instead.
                        val date = formatter.parse(entry) // You will need try/catch around this
                        millis = date.getTime()

                    } else {
                        var dateInTimeStamp = entry.replace("/Date(", "").replace("+0530)/", "")
                        if (dateInTimeStamp.contains(")/")) {
                            dateInTimeStamp = dateInTimeStamp.replace(")/", "")
                        }
                        millis = safeParseLong(dateInTimeStamp, Integer.MIN_VALUE.toLong())

                    }

                } catch (e: ParseException) {

                    e.printStackTrace()
                }

            }
            return millis
        }

        fun safeParseLong(@NonNull value: String, defaultValue: Long): Long {

            if (!value.isNotEmpty()) {
                return defaultValue
            }
            try {
                return java.lang.Long.parseLong(value)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return defaultValue
            }

        }

    }

}