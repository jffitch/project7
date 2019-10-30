package com.mathgeniusguide.project7.adapter

import com.mathgeniusguide.project7.connectivity.convertDate
import org.junit.Test

class PopularAdapterTest {
    @Test
    fun convertDate_convertsDateProperly() {
        val apiDateText = "2019-10-24T12:03:33-04:00"
        val expectedText = "October 24, 2019"

        val actualText = apiDateText.convertDate()

        assert(actualText == expectedText)
    }
}