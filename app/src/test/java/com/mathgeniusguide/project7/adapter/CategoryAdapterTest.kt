package com.mathgeniusguide.project7.adapter

import com.mathgeniusguide.project7.connectivity.convertDate
import org.junit.Test

class CategoryAdapterTest {

    @Test
    fun convertDate_convertsDateProperly() {
        val apiDateText = "2019-10-25T15:33:36-04:00"
        val expectedText = "October 25, 2019"

        val actualText = apiDateText.convertDate()

        assert(actualText == expectedText)
    }
}