package com.mathgeniusguide.project7.ui

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.mathgeniusguide.project7.R
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class SplashFragmentTest {

    @Test
    fun testSplashFragmentDismisses() {
        val scenario = launchFragment<SplashFragment>()
        scenario.moveToState(Lifecycle.State.DESTROYED)
        Espresso.onView(withId(R.id.splashTitle)).check(doesNotExist())
    }
}