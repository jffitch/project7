package com.mathgeniusguide.project7.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mathgeniusguide.project7.MainActivity
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.adapter.CategoryAdapter
import com.mathgeniusguide.project7.adapter.PopularAdapter
import com.mathgeniusguide.project7.base.BaseFragment
import com.mathgeniusguide.project7.responses.category.CategoryResult
import com.mathgeniusguide.project7.responses.popular.PopularResult
import com.mathgeniusguide.project7.util.Constants
import com.mathgeniusguide.project7.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.news.*

class NewsFragment: BaseFragment() {

    private val viewModel by lazy { ViewModelProviders.of(activity!!).get(NewsViewModel::class.java)}
    private val categoryNewsList = ArrayList<CategoryResult>()
    private val popularNewsList = ArrayList<PopularResult>()
    private var position: Int = 0
    private var newsBackArrowPressed = false

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
        return inflater.inflate(R.layout.news, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showActionBarAndNavigationView()

        // fetch news based on which tab is selected
        fetch(position)

        // set up back arrow to return to search results
        newsBackArrow.setOnClickListener {
            newsBackArrowPressed = true
            newsRV.visibility = View.VISIBLE
            newsBackArrow.visibility = View.GONE
            newsWebView.settings.javaScriptEnabled = false
            newsWebView.visibility = View.GONE
        }

        viewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.isDataLoadingError.observe(viewLifecycleOwner, Observer {error ->
            if(!error) {
                noNewsLayout.visibility = View.GONE
                newsRV.visibility = if (newsWebView.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            } else {
                noNewsLayout.visibility = View.VISIBLE
                noNewsIcon.setImageResource(R.drawable.no_news_icon)
                noNewsText.setText(R.string.error_loading)
                newsRV.visibility = View.GONE
            }
        })
    }

    private fun showActionBarAndNavigationView() {
        (activity as? MainActivity)?.showActionBar()
        (activity as? MainActivity)?.showBottomNavigationView()
    }

    private fun fetch(n: Int) {
        when (n) {
            Constants.TOP_NEWS -> {
                // fetch relevant news and observe
                viewModel.fetchTopNews()
                viewModel.topNews?.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        // Recycler View
                        // add each line from search to array list, then set up layout manager and adapter
                        categoryNewsList.addAll(it.results)
                        categoryNewsList.sortByDescending { v -> v.created_date }
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
                        popularNewsList.sortByDescending { v -> v.published_date }
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
                        categoryNewsList.sortByDescending { v -> v.created_date }
                        newsRV.layoutManager = LinearLayoutManager(context)
                        newsRV.adapter = CategoryAdapter(categoryNewsList, context!!, newsRV, newsWebView, newsBackArrow)
                    }
                })
            }
        }
    }

    override fun handleBack() {
        if(newsBackArrowPressed) {
            newsBackArrowPressed = false
        } else {
            activity?.finish()
        }
    }

}