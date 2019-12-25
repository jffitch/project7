package com.mathgeniusguide.project7.ui

import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class SearchFragmentTest {

    @Test
    fun fixDates_fixesDatesProperly1() {
        val searchFragment = SearchFragment()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val now = df.parse("2019-12-25 16:15:48")
        searchFragment.dateBegin = "2018-11-16"
        searchFragment.dateEnd = "2019-4-13"
        searchFragment.fixDates(now)
        assert(searchFragment.dateBegin == "2018-12-25T00:00:00Z" && searchFragment.dateEnd == "2019-04-13T23:59:59Z")
    }

    @Test
    fun fixDates_fixesDatesProperly2() {
        val searchFragment = SearchFragment()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val now = df.parse("2019-12-25 16:15:48")
        searchFragment.dateBegin = "2018-12-27"
        searchFragment.dateEnd = "2019-02-31"
        searchFragment.fixDates(now)
        assert(searchFragment.dateBegin == "2018-12-27T00:00:00Z" && searchFragment.dateEnd == "2019-12-25T23:59:59Z")
    }

    @Test
    fun fixDates_fixesDatesProperly3() {
        val searchFragment = SearchFragment()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val now = df.parse("2019-12-25 16:15:48")
        searchFragment.dateBegin = "2019-4-16"
        searchFragment.dateEnd = "2019-03-28"
        searchFragment.fixDates(now)
        assert(searchFragment.dateBegin == "2019-03-28T00:00:00Z" && searchFragment.dateEnd == "2019-04-16T23:59:59Z")
    }
}