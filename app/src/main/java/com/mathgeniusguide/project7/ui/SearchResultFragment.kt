package com.mathgeniusguide.project7.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    private val viewModel by lazy { ViewModelProviders.of(activity!!).get(NewsViewModel::class.java) }
    private val searchNewsList = ArrayList<SearchResult>()
    private var dateBegin = ""
    private var dateEnd = ""
    private var searchTerm = ""

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
        arguments?.let {
            searchTerm = it.getString("searchTerm", "")
            val categories = it.getString("categories", "")
            dateBegin = it.getString("dateBegin", "")
            dateEnd = it.getString("dateEnd", "")
            observeViewModel()
            viewModel.fetchSearchNews(searchTerm, categories, dateBegin, dateEnd)
        }
    }

    private fun observeViewModel() {
        viewModel.searchNews?.observe(viewLifecycleOwner, Observer { searchResponse ->
            if (searchResponse != null) {
                // Recycler View
                // add each line from database to array list, then set up layout manager and adapter
                searchNewsList.clear()
                searchNewsList.addAll(searchResponse.response.docs)
                searchNewsList.sortByDescending { it.pub_date }
                searchRV.layoutManager = LinearLayoutManager(context)
                searchRV.adapter =
                        SearchAdapter(searchNewsList, context!!, findNavController())
            }
        })
        viewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })

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