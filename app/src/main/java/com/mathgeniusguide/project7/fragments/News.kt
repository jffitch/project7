package com.mathgeniusguide.project7.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.adapter.CategoryAdapter
import com.mathgeniusguide.project7.adapter.PopularAdapter
import com.mathgeniusguide.project7.adapter.SearchAdapter
import com.mathgeniusguide.project7.responses.category.CategoryResult
import com.mathgeniusguide.project7.responses.popular.PopularResult
import com.mathgeniusguide.project7.responses.search.SearchResult
import com.mathgeniusguide.project7.util.Constants
import com.mathgeniusguide.project7.util.OnSwipeTouchListener
import com.mathgeniusguide.project7.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.news.*
import kotlinx.android.synthetic.main.search.*

class News: Fragment() {
    lateinit var viewModel: NewsViewModel
    val categoryNewsList = ArrayList<CategoryResult>()
    val popularNewsList = ArrayList<PopularResult>()
    var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("position")
        }
        viewModel = NewsViewModel(activity!!.application)
        setHasOptionsMenu(true)

        val intent = activity!!.intent
        if (intent != null && intent.extras != null) {
            val extras = intent.extras!!
            val searchTerm = extras.getString("searchTerm")!!
            val categories = extras.getString("categories")!!
            val dateBegin = extras.getString("dateBegin")!!
            val dateEnd = extras.getString("dateEnd")!!
            val navController = Navigation.findNavController(activity as Activity, R.id.nav_host_fragment);
            navController.navigate(R.id.load_from_notification);

            viewModel.fetchSearchNews(searchTerm, categories, dateBegin, dateEnd)
            viewModel.searchNews?.observe(viewLifecycleOwner, Observer {searchResponse ->
                if(searchResponse != null) {
                    // Recycler View
                    // add each line from database to array list, then set up layout manager and adapter
                    val searchNewsList = java.util.ArrayList<SearchResult>()
                    searchNewsList.addAll(searchResponse.response.docs)
                    searchRV.layoutManager = LinearLayoutManager(context)
                    searchRV.adapter = SearchAdapter(searchNewsList, context!!, searchRV, searchWebView, searchBackArrow)

                    searchBox.visibility = View.GONE
                    beginDate.visibility = View.GONE
                    endDate.visibility = View.GONE
                    checklist.visibility = View.GONE
                    searchButton.visibility = View.GONE
                    searchRV.visibility = View.VISIBLE
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.news, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSwipeListener(view)

        fetch(position)
        newsBackArrow.setOnClickListener {
            newsRV.visibility = View.VISIBLE
            newsBackArrow.visibility = View.GONE
            newsWebView.settings.javaScriptEnabled = false
            newsWebView.visibility = View.GONE
        }
    }

    fun fetch(n: Int) {
        when (n) {
            Constants.TOP_NEWS -> {
                viewModel.fetchTopNews()
                viewModel.topNews?.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        // Recycler View
                        // add each line from database to array list, then set up layout manager and adapter
                        categoryNewsList.addAll(it.results)
                        newsRV.layoutManager = LinearLayoutManager(context)
                        newsRV.adapter = CategoryAdapter(categoryNewsList, context!!, newsRV, newsWebView, newsBackArrow)
                    }
                })
            }
            Constants.POPULAR_NEWS -> {
                viewModel.fetchPopularNews()
                viewModel.popularNews?.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        // Recycler View
                        // add each line from database to array list, then set up layout manager and adapter
                        popularNewsList.addAll(it.results)
                        newsRV.layoutManager = LinearLayoutManager(context)
                        newsRV.adapter = PopularAdapter(popularNewsList, context!!, newsRV, newsWebView, newsBackArrow)
                    }
                })
            }
            Constants.POLITICS_NEWS -> {
                viewModel.fetchPoliticsNews()
                viewModel.politicsNews?.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        // Recycler View
                        // add each line from database to array list, then set up layout manager and adapter
                        categoryNewsList.addAll(it.results)
                        newsRV.layoutManager = LinearLayoutManager(context)
                        newsRV.adapter = CategoryAdapter(categoryNewsList, context!!, newsRV, newsWebView, newsBackArrow)
                    }
                })
            }
        }
    }

    private fun setSwipeListener(view: View) {
        context?.let {
            view.setOnTouchListener(object : OnSwipeTouchListener(it) {

            })
        }
    }
}