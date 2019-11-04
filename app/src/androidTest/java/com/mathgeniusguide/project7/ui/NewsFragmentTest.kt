package com.mathgeniusguide.project7.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.mathgeniusguide.project7.R
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class NewsFragmentTest {

    @Test
    fun testProgressBarIsDisplayedInitially() {

        val scenario = launchFragmentInContainer<NewsFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerViewIsDisplayedInitially() {

        val scenario = launchFragmentInContainer<NewsFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.newsRV)).check(matches(isDisplayed()))
    }

    @Test
    fun testNoNewsLayoutIsNotDisplayedInitially() {
        val scenario = launchFragmentInContainer<NewsFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.noNewsLayout)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}