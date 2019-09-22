package com.mathgeniusguide.project7.fragments

import android.app.job.JobInfo
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.adapter.SearchAdapter
import com.mathgeniusguide.project7.responses.search.SearchResult
import com.mathgeniusguide.project7.util.OnSwipeTouchListener
import com.mathgeniusguide.project7.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.search.*
import kotlinx.android.synthetic.main.checklist.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.content.ComponentName
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.app.job.JobScheduler
import android.os.PersistableBundle
import androidx.lifecycle.ViewModelProviders
import com.mathgeniusguide.project7.notifications.NotificationJobService
import com.mathgeniusguide.project7.util.Constants

class Search : Fragment() {
    val viewModel by lazy { ViewModelProviders.of(activity!!).get(NewsViewModel::class.java) }
    lateinit var viewList: ArrayList<CheckBox>
    val searchNewsList = ArrayList<SearchResult>()
    var dateBegin = ""
    var dateEnd = ""
    var searchTerm = ""
    var isNotification = false
    var newsCount = 0
    var pref: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.search, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // create list of all CheckBoxes to iterate through for loop
        viewList = arrayListOf(
            arts,
            automobiles,
            books,
            business,
            fashion,
            food,
            health,
            home,
            movies,
            obituaries,
            politics,
            realestate,
            science,
            sports,
            technology,
            theater,
            travel,
            world
        )
        setSwipeListener(view)
        // load SharedPreferences to decide state of CheckBoxes and Query
        loadSaved();
        buttonSearch();
        if (isNotification) {
            // make starting views invisible and RecyclerView visible
            searchQuery.visibility = View.GONE
            beginDate.visibility = View.GONE
            endDate.visibility = View.GONE
            checklist.visibility = View.GONE
            searchButton.visibility = View.GONE
            searchRV.visibility = View.VISIBLE
        }
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
            isNotification = it.getBoolean("isNotification", false)
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

    private fun buttonSearch() {
        searchButton.setOnClickListener {
            // save query, checkboxes, and switch in SharedPreferences
            val editor = pref?.edit()
            for (i in viewList) {
                editor?.putBoolean(resources.getResourceEntryName(i.id) + "_search", i.isChecked)
            }
            editor?.putString("searchQuery", searchQuery.text.toString())
            editor?.apply()

            // make starting views invisible and RecyclerView visible
            searchQuery.visibility = View.GONE
            beginDate.visibility = View.GONE
            endDate.visibility = View.GONE
            checklist.visibility = View.GONE
            searchButton.visibility = View.GONE
            searchRV.visibility = View.VISIBLE

            // get values from the EditTexts
            searchTerm = searchQuery.text.toString()
            dateBegin = beginDate.text.toString()
            dateEnd = endDate.text.toString()
            // make sure user inputted dates are valid and are between one year ago and today
            fixDates()

            // fetch data
            viewModel.fetchSearchNews(searchTerm, getCategories(), dateBegin, dateEnd)
            observeViewModel()

            // activate Back arrow to return to RecyclerView after loading a WebView
            searchBackArrow.setOnClickListener { view ->
                searchRV.visibility = View.VISIBLE
                searchBackArrow.visibility = View.GONE
                searchWebView.settings.javaScriptEnabled = false
                searchWebView.visibility = View.GONE
            }
        }
    }

    // load SharedPreferences to decide state of CheckBoxes and Query
    private fun loadSaved() {
        pref = context?.getSharedPreferences("com.mathgeniusguide.project7.pref", 0)
        for (i in viewList) {
            i.isChecked = pref?.getBoolean(resources.getResourceEntryName(i.id) + "_search", false) ?: false
        }
        searchQuery.setText(pref?.getString("searchQuery", "") ?: "")
    }

    // make sure user inputted dates are valid and are between one year ago and today
    private fun fixDates() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Date()
        sdf.isLenient = false
        // set timeBegin and timeEnd to number of days before today, initialize as 0
        var timeBegin = 0
        var timeEnd = 0
        // if dateBegin is valid date, clamp to between today and 365 days ago
        // if dateBegin is not valid date, set to 365 days ago
        try {
            timeBegin = ((today.time - sdf.parse(dateBegin).time) / 86400000).toInt()
            timeBegin = Math.min(365, timeBegin)
            timeBegin = Math.max(0, timeBegin)
        } catch (e: ParseException) {
            timeBegin = 365
        }
        // if dateEnd is valid date, clamp to between today and 365 days ago
        // if dateEnd is not valid date, set to today
        try {
            timeEnd = ((today.time - sdf.parse(dateEnd).time) / 86400000).toInt()
            timeEnd = Math.min(365, timeEnd)
            timeEnd = Math.max(0, timeEnd)
        } catch (e: ParseException) {
            timeEnd = 0
        }
        // set dateBegin and dateEnd to today minus timeBegin and timeEnd
        // swap them if dateBegin is after dateEnd
        dateBegin = sdf.format(Date(today.time - Math.max(timeBegin, timeEnd).toLong() * 86400000)) +
                "T00:00:00Z"
        dateEnd = sdf.format(Date(today.time - Math.min(timeBegin, timeEnd).toLong() * 86400000)) + "T23:59:59Z"
    }

    private fun getCategories(): String {
        // for each checkbox: if it's checked, add it to the string
        var string = ""
        for (i in viewList) {
            if (i.isChecked) {
                string += (if (string == "") "" else ",") + resources.getResourceEntryName(i.id)
            }
        }
        return string
    }

    private fun setSwipeListener(view: View) {
        context?.let {
            view.setOnTouchListener(object : OnSwipeTouchListener(it) {

            })
        }
    }
}