package com.mathgeniusguide.project7

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mathgeniusguide.project7.adapter.CategoryAdapter
import com.mathgeniusguide.project7.fragments.News
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EspressoTest {

    @JvmField
    @Rule
    val testRule = ActivityTestRule<MainActivity>(MainActivity::class.java)
    val intent = Intent(InstrumentationRegistry.getInstrumentation().context, MainActivity::class.java)

    // launch new activity before each test
    @Before
    fun init() {
        testRule.launchActivity(intent)
    }

    @Test
    fun politicsNewsItemsExist() {
        val fragment = News()
        val bundle = Bundle()
        bundle.putInt("position", 2)
        fragment.arguments = bundle
        testRule.activity.supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, fragment).commit()

        val rv = testRule.activity.findViewById<RecyclerView>(R.id.newsRV)
        val count = rv.adapter!!.itemCount
        assert(count > 0)
    }

    @Test
    fun webViewVisibleAfterClick() {
        val fragment = News()
        val bundle = Bundle()
        bundle.putInt("position", 2)
        fragment.arguments = bundle
        testRule.activity.supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, fragment).commit()

        onView(withId(R.id.newsRV)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryAdapter.ViewHolder>(1, click()))
        onView(withId(R.id.newsWebView)).check(matches(isDisplayed()))
    }
}