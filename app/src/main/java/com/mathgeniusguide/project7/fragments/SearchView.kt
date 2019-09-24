package com.mathgeniusguide.project7.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.adapter.SearchAdapter
import com.mathgeniusguide.project7.responses.search.SearchResult
import com.mathgeniusguide.project7.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.searchview.*
import java.util.*
import androidx.lifecycle.ViewModelProviders

class SearchView : Fragment() {
    val viewModel by lazy { ViewModelProviders.of(activity!!).get(NewsViewModel::class.java) }
    val searchNewsList = ArrayList<SearchResult>()
    var dateBegin = ""
    var dateEnd = ""
    var searchTerm = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.searchview, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set up back arrow to return to search results
        searchBackArrow.setOnClickListener {
            searchRV.visibility = View.VISIBLE
            searchBackArrow.visibility = View.GONE
            searchWebView.settings.javaScriptEnabled = false
            searchWebView.visibility = View.GONE
        }

        observeViewModel()
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
            viewModel.fetchSearchNews(searchTerm, categories, dateBegin, dateEnd)
        }
    }

    private fun observeViewModel() {
        viewModel.searchNews?.observe(viewLifecycleOwner, Observer { searchResponse ->
            if (searchResponse != null) {
                // Recycler View
                // add each line from database to array list, then set up layout manager and adapter
                searchNewsList.addAll(searchResponse.response.docs)
                searchRV.layoutManager = LinearLayoutManager(context)
                searchRV.adapter =
                        SearchAdapter(searchNewsList, context!!, searchRV, searchWebView, searchBackArrow)
            }
        })
    }
}