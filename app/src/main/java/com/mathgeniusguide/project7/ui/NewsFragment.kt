package com.mathgeniusguide.project7.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
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
import kotlinx.android.synthetic.main.news_fragment.*

class NewsFragment : BaseFragment() {
    // initialize viewModel
    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(NewsViewModel::class.java)
    }
    // initialize news lists
    private val categoryNewsList = ArrayList<CategoryResult>()
    private val popularNewsList = ArrayList<PopularResult>()
    private val politicsNewsList = ArrayList<CategoryResult>()
    // initialize tab position, will be set in navigation functions
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
        return inflater.inflate(R.layout.news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showActionBarAndNavigationView()

        // fetch news_fragment based on which tab is selected
        fetch(position)

        // show progress bar if data is currrently being loaded
        viewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        // show error screen if error loading data
        viewModel.isDataLoadingError.observe(viewLifecycleOwner, Observer { error ->
            if (!error) {
                noNewsLayout.visibility = View.GONE
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
                // fetch relevant news_fragment and observe
                viewModel.fetchTopNews()
                viewModel.topNews?.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        // Recycler View
                        // add each result to array list, then set up layout manager and adapter
                        categoryNewsList.clear()
                        categoryNewsList.addAll(it.results)
                        categoryNewsList.sortByDescending { v -> v.created_date }
                        newsRV.layoutManager = LinearLayoutManager(context)
                        newsRV.adapter = CategoryAdapter(
                            categoryNewsList,
                            context!!,
                            findNavController(),
                            position
                        )
                    }
                })
            }
            Constants.POPULAR_NEWS -> {
                // fetch relevant news_fragment and observe
                viewModel.fetchPopularNews()
                viewModel.popularNews?.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        // Recycler View
                        // add each result to array list, then set up layout manager and adapter
                        popularNewsList.clear()
                        popularNewsList.addAll(it.results)
                        popularNewsList.sortByDescending { v -> v.published_date }
                        newsRV.layoutManager = LinearLayoutManager(context)
                        newsRV.adapter =
                            PopularAdapter(popularNewsList, context!!, findNavController())
                    }
                })
            }
            Constants.POLITICS_NEWS -> {
                // fetch relevant news_fragment and observe
                viewModel.fetchPoliticsNews()
                viewModel.politicsNews?.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        // Recycler View
                        // add each result to array list, then set up layout manager and adapter
                        politicsNewsList.clear()
                        politicsNewsList.addAll(it.results)
                        politicsNewsList.sortByDescending { v -> v.created_date }
                        newsRV.layoutManager = LinearLayoutManager(context)
                        newsRV.adapter = CategoryAdapter(
                            politicsNewsList,
                            context!!,
                            findNavController(),
                            position
                        )
                    }
                })
            }
        }
    }

    override fun handleBack() {
        if (newsBackArrowPressed) {
            newsBackArrowPressed = false
        } else {
            activity?.finish()
        }
    }
}