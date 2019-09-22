package com.mathgeniusguide.project7.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
    val viewModel by lazy { ViewModelProviders.of(activity!!).get(NewsViewModel::class.java)}

    val categoryNewsList = ArrayList<CategoryResult>()
    val popularNewsList = ArrayList<PopularResult>()
    var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("position")
        }
        setHasOptionsMenu(true)
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

        // fetch news based on which tab is selected
        fetch(position)

        // set up back arrow to return to search results
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
                // fetch relevant news and observe
                viewModel.fetchTopNews()
                viewModel.topNews?.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        // Recycler View
                        // add each line from search to array list, then set up layout manager and adapter
                        categoryNewsList.addAll(it.results)
                        newsRV.layoutManager = LinearLayoutManager(context)
                        newsRV.adapter = CategoryAdapter(categoryNewsList, context!!, newsRV, newsWebView, newsBackArrow)
                    }
                })
            }
            Constants.POPULAR_NEWS -> {
                // fetch relevant news and observe
                viewModel.fetchPopularNews()
                viewModel.popularNews?.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        // Recycler View
                        // add each line from search to array list, then set up layout manager and adapter
                        popularNewsList.addAll(it.results)
                        newsRV.layoutManager = LinearLayoutManager(context)
                        newsRV.adapter = PopularAdapter(popularNewsList, context!!, newsRV, newsWebView, newsBackArrow)
                    }
                })
            }
            Constants.POLITICS_NEWS -> {
                // fetch relevant news and observe
                viewModel.fetchPoliticsNews()
                viewModel.politicsNews?.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        // Recycler View
                        // add each line from search to array list, then set up layout manager and adapter
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