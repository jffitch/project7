package com.mathgeniusguide.project7

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mathgeniusguide.project7.adapter.CategoryAdapter
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationSaveTestEspresso {

    @JvmField
    @Rule
    val testRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    // launch new activity before each test
    @Before
    fun setup() {
        testRule.launchActivity(Intent())
    }

    // when news item is clicked, check whether RecyclerView, WebView, and Back Arrow are visible
    @Test
    fun onNewsItemIsRecyclerViewVisible() {
        onView(withId(R.id.newsRV)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryAdapter.ViewHolder>(1, click()))
        onView(withId(R.id.newsWebView)).check(matches(isDisplayed()))
    }
    @Test
    fun onNewsItemIsWebViewVisible() {
        onView(withId(R.id.newsRV)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryAdapter.ViewHolder>(1, click()))
        onView(withId(R.id.newsWebView)).check(matches(isDisplayed()))
    }
    @Test
    fun onNewsItemIsBackArrowVisible() {
        onView(withId(R.id.newsRV)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryAdapter.ViewHolder>(1, click()))
        onView(withId(R.id.newsBackArrow)).check(matches(isDisplayed()))
    }

    // when Back Arrow is clicked, check whether RecyclerView, WebView, and Back Arrow are visible
    @Test
    fun onBackArrowIsRecyclerViewVisible() {
        onView(withId(R.id.newsRV)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryAdapter.ViewHolder>(1, click()))
        onView(withId(R.id.newsBackArrow)).perform(click())
        onView(withId(R.id.newsRV)).check(matches(isDisplayed()))
    }
    @Test
    fun onBackArrowIsWebViewVisible() {
        onView(withId(R.id.newsRV)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryAdapter.ViewHolder>(1, click()))
        onView(withId(R.id.newsBackArrow)).perform(click())
        onView(withId(R.id.newsWebView)).check(matches(isDisplayed()))
    }
    @Test
    fun onBackArrowIsBackArrowVisible() {
        onView(withId(R.id.newsRV)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryAdapter.ViewHolder>(1, click()))
        onView(withId(R.id.newsBackArrow)).perform(click())
        onView(withId(R.id.newsBackArrow)).check(matches(isDisplayed()))
    }
}