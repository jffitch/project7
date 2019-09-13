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


class Search: Fragment() {
    lateinit var viewModel: NewsViewModel
    lateinit var viewList: ArrayList<CheckBox>
    val searchNewsList = ArrayList<SearchResult>()
    var dateBegin = ""
    var dateEnd = ""
    var searchTerm = ""
    var screen: Int = 0
    var newsCount = 0
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            screen = it.getInt("screen")
        }
        viewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
    }

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
        viewList = arrayListOf(arts, automobiles, books, business, fashion, food, health, home, movies, obituaries, politics, realestate, science, sports, technology, theater, travel, world)
        setSwipeListener(view)
        when (screen) {
            Constants.SEARCH_SCREEN -> {
                // Search screen
                // make Notification views invisible
                notificationText.visibility = View.GONE
                notificationSwitch.visibility = View.GONE
                // set button to activate Search function when clicked
                buttonSearch()
            }
            Constants.NOTIFICATION_SCREEN -> {
                // Notification screen
                // load SharedPreferences to decide state of CheckBoxes and Search Box
                pref = context?.getSharedPreferences("com.mathgeniusguide.project7.pref", 0)
                for (i in viewList) {
                    i.isChecked = pref?.getBoolean(resources.getResourceEntryName(i.id), false) ?: false
                }
                notificationSwitch.isChecked = pref?.getBoolean("switch", false) ?: false
                searchBox.setText(pref?.getString("search", "") ?: "")
                // make Search views invisible
                beginDate.visibility = View.GONE
                endDate.visibility = View.GONE
                // set button to activate Notification function when clicked, and set its text to "SAVE"
                searchButton.text = "SAVE"
                buttonNotification()
            }
            Constants.LOADED_SCREEN -> {
                // make only RecyclerView visible
                searchBox.visibility = View.GONE
                beginDate.visibility = View.GONE
                endDate.visibility = View.GONE
                checklist.visibility = View.GONE
                searchButton.visibility = View.GONE
                searchRV.visibility = View.VISIBLE
                notificationText.visibility = View.GONE
                notificationSwitch.visibility = View.GONE

                // activate Back arrow to return to RecyclerView after loading a WebView
                searchBackArrow.setOnClickListener { view ->
                    searchRV.visibility = View.VISIBLE
                    searchBackArrow.visibility = View.GONE
                    searchWebView.settings.javaScriptEnabled = false
                    searchWebView.visibility = View.GONE
                }

                // observe searchNews and populate RecyclerView
                viewModel.searchNews?.observe(viewLifecycleOwner, Observer {searchResponse ->
                    if(searchResponse != null) {
                        // Recycler View
                        // add each line from search to array list, then set up layout manager and adapter
                        val searchNewsList = java.util.ArrayList<SearchResult>()
                        searchNewsList.addAll(searchResponse.response.docs)
                        searchRV.layoutManager = LinearLayoutManager(context)
                        searchRV.adapter = SearchAdapter(searchNewsList, context!!, searchRV, searchWebView, searchBackArrow)
                    }
                })
            }
        }
    }

    private fun buttonSearch() {
        searchButton.setOnClickListener {
            // make starting views invisible and the RecyclerView visible
            searchBox.visibility = View.GONE
            beginDate.visibility = View.GONE
            endDate.visibility = View.GONE
            checklist.visibility = View.GONE
            searchButton.visibility = View.GONE
            searchRV.visibility = View.VISIBLE

            // get values from the EditTexts
            searchTerm = searchBox.text.toString()
            dateBegin = beginDate.text.toString()
            dateEnd = endDate.text.toString()
            // make sure user inputted dates are valid and are between one year ago and today
            fixDates()

            // fetch data
            viewModel.fetchSearchNews(searchTerm, getCategories(), dateBegin, dateEnd)
            viewModel.searchNews?.observe(viewLifecycleOwner, Observer {searchResponse ->
                if(searchResponse != null) {
                    // Recycler View
                    // add each line from database to array list, then set up layout manager and adapter
                    searchNewsList.addAll(searchResponse.response.docs)
                    searchRV.layoutManager = LinearLayoutManager(context)
                    searchRV.adapter = SearchAdapter(searchNewsList, context!!, searchRV, searchWebView, searchBackArrow)
                }
            })

            // activate Back arrow to return to RecyclerView after loading a WebView
            searchBackArrow.setOnClickListener { view ->
                searchRV.visibility = View.VISIBLE
                searchBackArrow.visibility = View.GONE
                searchWebView.settings.javaScriptEnabled = false
                searchWebView.visibility = View.GONE
            }
        }
    }

    private fun buttonNotification() {
        searchButton.setOnClickListener {
            // save checkboxes and notification switch in SharedPreferences
            val editor = pref?.edit()
            for (i in viewList) {
                editor?.putBoolean(resources.getResourceEntryName(i.id), i.isChecked)
            }
            editor?.putBoolean("switch", notificationSwitch.isChecked)
            editor?.putString("search", searchBox.text.toString())
            editor?.apply()

            // set up notification Job Service
            notificationSetup()
        }
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
        }
        catch (e: ParseException){
            timeBegin = 365
        }
        // if dateEnd is valid date, clamp to between today and 365 days ago
        // if dateEnd is not valid date, set to today
        try {
            timeEnd = ((today.time - sdf.parse(dateEnd).time) / 86400000).toInt()
            timeEnd = Math.min(365, timeEnd)
            timeEnd = Math.max(0, timeEnd)
        }
        catch (e: ParseException){
            timeEnd = 0
        }
        // set dateBegin and dateEnd to today minus timeBegin and timeEnd
        // swap them if dateBegin is after dateEnd
        dateBegin = sdf.format(Date(today.time - Math.max(timeBegin, timeEnd).toLong() * 86400000)) + "T00:00:00Z"
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

    // set up notification Job Service
    private fun notificationSetup() {
        val scheduler = context!!.getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(context?.getApplicationContext(), NotificationJobService::class.java)

        // activate Job Service if Notification Switch is checked, cancel if not checked
        if (notificationSwitch.isChecked) {
            // get Search Term and categories, and save them to a bundle for the Job Service
            searchTerm = searchBox.text.toString()
            val categories = getCategories()
            val bundle = PersistableBundle();
            bundle.putString("searchTerm", searchTerm);
            bundle.putString("categories", categories);

            val jobInfo = JobInfo.Builder(0, componentName).setPeriodic(86400 * 1000).setExtras(bundle).build();
            scheduler.schedule(jobInfo);
        } else {
            scheduler.cancelAll()
        }
    }

    private fun setSwipeListener(view: View) {
        context?.let {
            view.setOnTouchListener(object : OnSwipeTouchListener(it) {

            })
        }
    }
}