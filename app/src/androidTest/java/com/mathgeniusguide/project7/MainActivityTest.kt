package com.mathgeniusguide.project7

import androidx.test.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** UI tests for main activity  */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private var mainActivity: MainActivity? = null
    @get:Rule
    val mainActivityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        mainActivity = mainActivityRule.activity
    }

    // Context of the app under test.
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.mathgeniusguide.project7", appContext.packageName)
    }
}