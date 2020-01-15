package com.mathgeniusguide.project7.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.TestLifecycle
import com.jraska.livedata.TestObserver
import com.mathgeniusguide.project7.responses.category.CategoryResponse
import com.mathgeniusguide.project7.responses.popular.PopularResponse
import com.mathgeniusguide.project7.responses.search.SearchResponse
import com.mathgeniusguide.project7.responses.search.SearchResponseFull
import com.mathgeniusguide.project7.util.TestUtility
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsViewModelTest {
    @Rule
    @JvmField
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
    fun setValueTestCategory() {
        val liveData = viewModel!!.testCategory
        val testObserver = TestObserver.test(liveData)
        val list = TestUtility.getTestingCategoryListOfSize(5)
        liveData!!.value = CategoryResponse(list, "OK")
        val value = testObserver.value()
        assertThat(value?.results).isEqualTo(list)
        liveData.removeObserver(testObserver)
        assertThat(liveData.hasObservers()).isFalse()
    }

    @Test
    fun counterHistoryTestCategory() {
        val liveData = viewModel!!.testCategory
        val testObserver = TestObserver.test(liveData)
        val list = TestUtility.getTestingCategoryListOfSize(5)
        liveData!!.value = CategoryResponse(list, "OK")
        testObserver.assertHasValue().assertHistorySize(1)
        for(i in 0 until 4)
        {
            liveData.value = CategoryResponse(TestUtility.getTestingCategoryListOfSize(30), "OK")
        }
        testObserver.assertHasValue().assertHistorySize(5)
    }

    @Test
    fun useObserverWithLifecycleCategory() {
        val testObserver = TestObserver.create<CategoryResponse>()
        val testLifecycle = TestLifecycle.initialized()
        viewModel!!.testCategory!!.observe(testLifecycle, testObserver)
        testObserver.assertNoValue()
        testLifecycle.resume()
        for (i in 0 until 4) {
            viewModel!!.testCategory!!.value = CategoryResponse(TestUtility.getTestingCategoryListOfSize(4), "OK")
        }
        val list = TestUtility.getTestingCategoryListOfSize(10)
        viewModel!!.testCategory!!.value = CategoryResponse(list, "OK")
        testObserver.assertHasValue().assertValue(CategoryResponse(list, "OK")).assertHistorySize(5)
        viewModel!!.testCategory!!.removeObserver(testObserver)
    }

    @Test
    fun setValueTestPopular() {
        val liveData = viewModel!!.testPopular
        val testObserver = TestObserver.test(liveData)
        val list = TestUtility.getTestingPopularListOfSize(5)
        liveData!!.value = PopularResponse(list, "OK")
        val value = testObserver.value()
        assertThat(value?.results).isEqualTo(list)
        liveData.removeObserver(testObserver)
        assertThat(liveData.hasObservers()).isFalse()
    }

    @Test
    fun counterHistoryTestPopular() {
        val liveData = viewModel!!.testPopular
        val testObserver = TestObserver.test(liveData)
        val list = TestUtility.getTestingPopularListOfSize(5)
        liveData!!.value = PopularResponse(list, "OK")
        testObserver.assertHasValue().assertHistorySize(1)
        for(i in 0 until 4)
        {
            liveData.value = PopularResponse(TestUtility.getTestingPopularListOfSize(30), "OK")
        }
        testObserver.assertHasValue().assertHistorySize(5)
    }

    @Test
    fun useObserverWithLifecyclePopular() {
        val testObserver = TestObserver.create<PopularResponse>()
        val testLifecycle = TestLifecycle.initialized()
        viewModel!!.testPopular!!.observe(testLifecycle, testObserver)
        testObserver.assertNoValue()
        testLifecycle.resume()
        for (i in 0 until 4) {
            viewModel!!.testPopular!!.value = PopularResponse(TestUtility.getTestingPopularListOfSize(4), "OK")
        }
        val list = TestUtility.getTestingPopularListOfSize(10)
        viewModel!!.testPopular!!.value = PopularResponse(list, "OK")
        testObserver.assertHasValue().assertValue(PopularResponse(list, "OK")).assertHistorySize(5)
        viewModel!!.testPopular!!.removeObserver(testObserver)
    }

    @Test
    fun setValueTestSearch() {
        val liveData = viewModel!!.testSearch
        val testObserver = TestObserver.test(liveData)
        val list = TestUtility.getTestingSearchListOfSize(5)
        liveData!!.value = SearchResponseFull(SearchResponse(list), "OK")
        val value = testObserver.value()
        assertThat(value?.response!!.docs).isEqualTo(list)
        liveData.removeObserver(testObserver)
        assertThat(liveData.hasObservers()).isFalse()
    }

    @Test
    fun counterHistoryTestSearch() {
        val liveData = viewModel!!.testSearch
        val testObserver = TestObserver.test(liveData)
        val list = TestUtility.getTestingSearchListOfSize(5)
        liveData!!.value = SearchResponseFull(SearchResponse(list), "OK")
        testObserver.assertHasValue().assertHistorySize(1)
        for(i in 0 until 4)
        {
            liveData.value = SearchResponseFull(SearchResponse(TestUtility.getTestingSearchListOfSize(30)), "OK")
        }
        testObserver.assertHasValue().assertHistorySize(5)
    }

    @Test
    fun useObserverWithLifecycleSearch() {
        val testObserver = TestObserver.create<SearchResponseFull>()
        val testLifecycle = TestLifecycle.initialized()
        viewModel!!.testSearch!!.observe(testLifecycle, testObserver)
        testObserver.assertNoValue()
        testLifecycle.resume()
        for (i in 0 until 4) {
            viewModel!!.testSearch!!.value = SearchResponseFull(SearchResponse(TestUtility.getTestingSearchListOfSize(4)), "OK")
        }
        val list = TestUtility.getTestingSearchListOfSize(10)
        viewModel!!.testSearch!!.value = SearchResponseFull(SearchResponse(list), "OK")
        testObserver.assertHasValue().assertValue(SearchResponseFull(SearchResponse(list), "OK")).assertHistorySize(5)
        viewModel!!.testSearch!!.removeObserver(testObserver)
    }
}