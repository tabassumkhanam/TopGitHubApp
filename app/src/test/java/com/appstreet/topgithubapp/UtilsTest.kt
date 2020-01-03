package com.appstreet.topgithubapp

import com.appstreet.topgithubapp.utilities.AppUtils
import junit.framework.Assert
import org.junit.Assert.assertEquals
import org.junit.Test



class UtilsTest {
    @Test
    fun testCurrentDayValue() {
        val actual = AppUtils.getCurrentDay()
        val expected = "Fri"
        assertEquals("Day Conversion Failed", expected, actual)
    }

    @Test
    @Throws(Exception::class)
    fun dateUtilsFormat_isCorrect() {
        val epoc: Long = 1446885450
        val date = AppUtils.epocSecondsToDate(epoc)
        assertEquals(
            "Date time in millis is wrong",
            epoc * 1000, date.getTime()
        )
        val day = AppUtils.dateToDayDateString(date, true)
        assertEquals("Day is wrong", "SAT", day)
    }

    @Test
    fun getTimeinMillis() {
        Assert.assertNotSame(-1, AppUtils.getTimeinMillis("4/6/2017"))
        Assert.assertNotSame(-1, AppUtils.getTimeinMillis("12/16/2017"))
    }
}