package com.mathgeniusguide.project7.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.navigation.fragment.findNavController
import com.mathgeniusguide.project7.MainActivity
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.base.BaseFragment
import com.mathgeniusguide.project7.responses.search.SearchResult
import kotlinx.android.synthetic.main.checklist.*
import kotlinx.android.synthetic.main.search.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SearchFragment : BaseFragment() {

    lateinit var viewList: ArrayList<CheckBox>
    val searchNewsList = ArrayList<SearchResult>()
    var dateBegin = ""
    var dateEnd = ""
    var searchTerm = ""
    var isNotification = false
    var pref: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.search, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as? MainActivity)?.hideBottomNavigationView()

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
        // load SharedPreferences to decide state of CheckBoxes and Query
        loadSaved();
        buttonSearch();
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

            // get values from the EditTexts
            searchTerm = searchQuery.text.toString()
            dateBegin = beginDate.text.toString()
            dateEnd = endDate.text.toString()
            // make sure user inputted dates are valid and are between one year ago and today
            fixDates()

            val bundle = Bundle()
            bundle.putString("searchTerm", searchTerm)
            bundle.putString("categories", getCategories())
            bundle.putString("dateBegin", dateBegin)
            bundle.putString("dateEnd", dateEnd)
            findNavController().navigate(R.id.show_result, bundle)
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

    override fun handleBack() {
        findNavController().navigate(R.id.action_search_to_topNews)
    }
}