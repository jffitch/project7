package com.mathgeniusguide.project7.adapter

import com.mathgeniusguide.project7.connectivity.convertDate
import org.junit.Test

class CategoryAdapterTest {

    @Test
    fun convertDate_convertsDateProperly1() {
        val apiDateText = "2019-10-25T15:33:36-04:00"
        val expectedText = "October 25, 2019"

        val actualText = apiDateText.convertDate()

        assert(actualText == expectedText)
    }

    @Test
    fun convertDate_convertsDateProperly2() {
        val apiDateText = "2018-04-03T04:48:19-04:00"
        val expectedText = "April 3, 2018"

        val actualText = apiDateText.convertDate()

        assert(actualText == expectedText)
    }
}