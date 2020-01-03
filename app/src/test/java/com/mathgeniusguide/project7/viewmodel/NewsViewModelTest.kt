package com.mathgeniusguide.project7.viewmodel

import android.app.Application
import com.jraska.livedata.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.jraska.livedata.TestLifecycle
import com.mathgeniusguide.project7.util.TestUtility
import com.mathgeniusguide.project7.responses.category.CategoryResponse
import com.mathgeniusguide.project7.responses.popular.PopularResponse
import com.mathgeniusguide.project7.responses.search.SearchResponseFull

class NewsViewModelTest {
    @Rule
    var testRule = InstantTaskExecutorRule()
    private var viewModel: NewsViewModel? = null
    // Set up dummy viewmodel to perform tests
    @Before
    fun setUp() {
        viewModel = NewsViewModel(Application())
    }

    @Test
    fun topNewsStartsEmpty() {
        val liveData = viewModel!!.topNews
        TestObserver.test<CategoryResponse>(liveData)
            .assertNoValue()
    }

    @Test
    fun popularNewsStartsEmpty() {
        val liveData = viewModel!!.popularNews
        TestObserver.test<PopularResponse>(liveData)
            .assertNoValue()
    }

    @Test
    fun politicsNewsStartsEmpty() {
        val liveData = viewModel!!.politicsNews
        TestObserver.test<CategoryResponse>(liveData)
            .assertNoValue()
    }

    @Test
    fun searchNewsStartsEmpty() {
        val liveData = viewModel!!.searchNews
        TestObserver.test<SearchResponseFull>(liveData)
            .assertNoValue()
    }

    @Test
    fun setValueTest() {
        val liveData = viewModel!!.testNews
        val testObserver = TestObserver.test(liveData)
        val list = TestUtility.getTestingCategoryListOfSize(5)
        liveData!!.setValue(CategoryResponse(list, "OK"))
        val value = testObserver.value()
        assertThat(value).isEqualTo(list)
        liveData.removeObserver(testObserver)
        assertThat(liveData.hasObservers()).isFalse()
    }

    @Test
    fun counterHistoryTest() {
        val liveData = viewModel!!.testNews
        val testObserver = TestObserver.test(liveData)
        val list = TestUtility.getTestingCategoryListOfSize(5)
        liveData!!.setValue(CategoryResponse(list, "OK"))
        testObserver.assertHasValue().assertHistorySize(1);
        for(i in 0 until 4)
        {
            liveData.setValue(CategoryResponse(TestUtility.getTestingCategoryListOfSize(30), "OK"))
        }
        testObserver.assertHasValue().assertHistorySize(5);
    }

    @Test
    fun useObserverWithLifecycle() {
        val testObserver = TestObserver.create<CategoryResponse>();
        val testLifecycle = TestLifecycle.initialized();
        viewModel!!.testNews!!.observe(testLifecycle, testObserver);
        testObserver.assertNoValue();
        testLifecycle.resume();

        for (i in 0 until 4) {
            viewModel!!.testNews!!.setValue(CategoryResponse(TestUtility.getTestingCategoryListOfSize(4), "OK"))
        }
        val list = TestUtility.getTestingCategoryListOfSize(10);
        viewModel!!.testNews!!.setValue(CategoryResponse(list, "OK"))
        testObserver.assertHasValue().assertValue(list).assertHistorySize(5);
        viewModel!!.testNews!!.removeObserver(testObserver);
    }
}