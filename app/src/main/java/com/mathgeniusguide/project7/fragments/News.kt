package com.mathgeniusguide.project7.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.adapter.CategoryAdapter
import com.mathgeniusguide.project7.adapter.PopularAdapter
import com.mathgeniusguide.project7.responses.category.CategoryResult
import com.mathgeniusguide.project7.responses.popular.PopularResult
import com.mathgeniusguide.project7.util.Constants
import com.mathgeniusguide.project7.util.OnSwipeTouchListener
import com.mathgeniusguide.project7.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.news.*

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
        backArrow.setOnClickListener {
            rv.visibility = View.VISIBLE
            backArrow.visibility = View.GONE
            webView.settings.javaScriptEnabled = false
            webView.visibility = View.GONE
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
                        rv.layoutManager = LinearLayoutManager(context)
                        rv.adapter = CategoryAdapter(categoryNewsList, context!!, rv, webView, backArrow)
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
                        rv.layoutManager = LinearLayoutManager(context)
                        rv.adapter = PopularAdapter(popularNewsList, context!!, rv, webView, backArrow)
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
                        rv.layoutManager = LinearLayoutManager(context)
                        rv.adapter = CategoryAdapter(categoryNewsList, context!!, rv, webView, backArrow)
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