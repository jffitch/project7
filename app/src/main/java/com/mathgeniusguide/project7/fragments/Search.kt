package com.mathgeniusguide.project7.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.content.Context
import android.content.Intent
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
import com.mathgeniusguide.project7.notifications.NotificationReceiver
import com.mathgeniusguide.project7.responses.search.SearchResult
import com.mathgeniusguide.project7.util.OnSwipeTouchListener
import com.mathgeniusguide.project7.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.search.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.content.ComponentName
import android.content.Context.JOB_SCHEDULER_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.job.JobScheduler
import android.os.PersistableBundle
import androidx.lifecycle.ViewModelProviders
import com.mathgeniusguide.project7.notifications.NotificationJobService


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
        viewList = arrayListOf(arts, automobiles, books, business, fashion, food, health, home, movies, obituaries, politics, realestate, science, sports, technology, theater, travel, world)
        setSwipeListener(view)
        when (screen) {
            0 -> {
                notificationText.visibility = View.GONE
                notificationSwitch.visibility = View.GONE
                buttonSearch()
            }
            1 -> {
                pref = context?.getSharedPreferences("com.mathgeniusguide.project7.pref", 0)
                for (i in viewList) {
                    i.isChecked = pref?.getBoolean(resources.getResourceEntryName(i.id), false) ?: false
                }
                notificationSwitch.isChecked = pref?.getBoolean("switch", false) ?: false
                searchBox.setText(pref?.getString("search", "") ?: "")
                beginDate.visibility = View.GONE
                endDate.visibility = View.GONE
                searchButton.text = "SAVE"
                buttonNotification()
            }
        }
    }

    private fun buttonSearch() {
        searchButton.setOnClickListener {
            searchBox.visibility = View.GONE
            beginDate.visibility = View.GONE
            endDate.visibility = View.GONE
            checklist.visibility = View.GONE
            searchButton.visibility = View.GONE
            rv.visibility = View.VISIBLE

            searchTerm = searchBox.text.toString()
            dateBegin = beginDate.text.toString()
            dateEnd = endDate.text.toString()
            fixDates()

            viewModel.fetchSearchNews(searchTerm, getCategories(), dateBegin, dateEnd)
            viewModel.searchNews?.observe(viewLifecycleOwner, Observer {searchResponse ->
                if(searchResponse != null) {
                    // Recycler View
                    // add each line from database to array list, then set up layout manager and adapter
                    searchNewsList.addAll(searchResponse.response.docs)
                    rv.layoutManager = LinearLayoutManager(context)
                    rv.adapter = SearchAdapter(searchNewsList, context!!, rv, webView, backArrow)
                }
            })

            backArrow.setOnClickListener {view ->
                rv.visibility = View.VISIBLE
                backArrow.visibility = View.GONE
                webView.settings.javaScriptEnabled = false
                webView.visibility = View.GONE
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

            notificationSetup()
        }
    }

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

    private fun notificationSetup() {
        val scheduler = context!!.getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(context?.getApplicationContext(), NotificationJobService::class.java)

        if (notificationSwitch.isChecked) {
            val categories = getCategories()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = Date()
            val dateBegin = sdf.format(today) + "T00:00:00Z"
            val dateEnd = sdf.format(Date(today.time + 86400000)) + "T23:59:59Z"
            val bundle = PersistableBundle();

            viewModel.fetchSearchNews(searchTerm, categories, dateBegin, dateEnd)
            viewModel.searchNews?.observe(viewLifecycleOwner, Observer { searchNews ->
                newsCount = searchNews.response.docs.size
            })

            bundle.putInt("dailynews", newsCount);
            bundle.putString("searchTerm", searchTerm);
            bundle.putString("categories", categories);
            bundle.putString("dateBegin", dateBegin);
            bundle.putString("dateEnd", dateEnd);

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