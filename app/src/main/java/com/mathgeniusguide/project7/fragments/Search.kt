package com.mathgeniusguide.project7.fragments

import android.app.AlarmManager
import android.app.PendingIntent
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

class Search: Fragment() {
    lateinit var viewModel: NewsViewModel
    lateinit var viewList: ArrayList<CheckBox>
    val searchNewsList = ArrayList<SearchResult>()
    var dateBegin = ""
    var dateEnd = ""
    var searchTerm = ""
    var screen: Int = 0
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            screen = it.getInt("screen")
        }
        viewModel = NewsViewModel(activity!!.application)
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
            val editor = pref?.edit()
            for (i in viewList) {
                editor?.putBoolean(resources.getResourceEntryName(i.id), i.isChecked)
            }
            editor?.putBoolean("switch", notificationSwitch.isChecked)
            editor?.putString("search", searchBox.text.toString())
            editor?.apply()

            val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.putExtra("searchTerm", searchBox.text.toString())
            intent.putExtra("categories", getCategories())
            val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
            alarmManager.cancel(pendingIntent)

            if (notificationSwitch.isChecked) {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, 8)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
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