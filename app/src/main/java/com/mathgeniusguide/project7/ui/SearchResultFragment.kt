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
import com.mathgeniusguide.project7.adapter.SearchAdapter
import com.mathgeniusguide.project7.base.BaseFragment
import com.mathgeniusguide.project7.responses.search.SearchResult
import com.mathgeniusguide.project7.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.search_result_fragment.*
import java.util.*

class SearchResultFragment : BaseFragment() {

    // initialzie viewModel
    private val viewModel by lazy { ViewModelProviders.of(activity!!).get(NewsViewModel::class.java) }
    // initialize news list
    private val searchNewsList = ArrayList<SearchResult>()
    // initialize date, search term, and categories variables, which will later be gotten from bundle
    private var dateBegin = ""
    private var dateEnd = ""
    private var searchTerm = ""
    private var categories = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.hideBottomNavigationView()
        (activity as? MainActivity)?.showActionBar()
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // load variables from bundle
        arguments?.let {
            searchTerm = it.getString("searchTerm", "")
            categories = it.getString("categories", "")
            dateBegin = it.getString("dateBegin", "")
            dateEnd = it.getString("dateEnd", "")
        }
        // observe viewModel and fetch news
        observeViewModel()
        viewModel.fetchSearchNews(searchTerm, categories, dateBegin, dateEnd)
    }

    private fun observeViewModel() {
        viewModel.searchNews?.observe(viewLifecycleOwner, Observer { searchResponse ->
            if (searchResponse != null) {
                // Recycler View
                // add each result to array list, then set up layout manager and adapter
                searchNewsList.clear()
                searchNewsList.addAll(searchResponse.response.docs)
                searchNewsList.sortByDescending { it.pub_date }
                searchRV.layoutManager = LinearLayoutManager(context)
                searchRV.adapter =
                        SearchAdapter(searchNewsList, context!!, findNavController())
            }
        })

        // show progress bar if data is currrently being loaded
        viewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })

        // show error screen if error loading data
        viewModel.isDataLoadingError.observe(viewLifecycleOwner, Observer {error ->
            if(!error) {
                noNewsLayout.visibility = View.GONE
                searchRV.visibility = View.VISIBLE
            } else {
                noNewsLayout.visibility = View.VISIBLE
                noNewsIcon.setImageResource(R.drawable.no_news_icon)
                noNewsText.setText(R.string.error_loading)
                searchRV.visibility = View.GONE
            }
        })
    }

    override fun handleBack() {
        findNavController().navigate(R.id.action_search_result_to_search)
    }
}