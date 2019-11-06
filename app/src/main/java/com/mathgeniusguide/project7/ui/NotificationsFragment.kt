package com.mathgeniusguide.project7.ui

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.navigation.fragment.findNavController
import com.mathgeniusguide.project7.MainActivity
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.base.BaseFragment
import com.mathgeniusguide.project7.notifications.NotificationJobService
import kotlinx.android.synthetic.main.checklist.*
import kotlinx.android.synthetic.main.notification.*
import java.util.*

class NotificationsFragment : BaseFragment() {

    lateinit var viewList: ArrayList<CheckBox>
    var searchTerm = ""
    var newsCount = 0
    var pref: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.notification, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as? MainActivity)?.hideBottomNavigationView()

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
        // load SharedPreferences to decide state of CheckBoxes and Query
        loadSaved();
        // set button to activate Notification function when clicked
        buttonNotification()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    // load SharedPreferences to decide state of CheckBoxes and Query
    private fun loadSaved() {
        pref = context?.getSharedPreferences("com.mathgeniusguide.project7.pref", 0)
        for (i in viewList) {
            i.isChecked = pref?.getBoolean(resources.getResourceEntryName(i.id) + "_notification", false) ?: false
        }
        notificationSwitch.isChecked = pref?.getBoolean("switch", false) ?: false
        notificationQuery.setText(pref?.getString("notificationQuery", "") ?: "")
    }

    private fun buttonNotification() {
        saveButton.setOnClickListener {
            // save query, checkboxes, and switch in SharedPreferences
            val editor = pref?.edit()
            for (i in viewList) {
                editor?.putBoolean(resources.getResourceEntryName(i.id) + "_notification", i.isChecked)
            }
            editor?.putBoolean("switch", notificationSwitch.isChecked)
            editor?.putString("notificationQuery", notificationQuery.text.toString())
            editor?.apply()

            // set up notification Job Service
            notificationSetup()
        }
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
        val componentName = ComponentName(context!!.getApplicationContext(), NotificationJobService::class.java)

        // activate Job Service if Notification Switch is checked, cancel if not checked
        if (notificationSwitch.isChecked) {
            // get SearchFragment Term and categories, and save them to a bundle for the Job Service
            searchTerm = notificationQuery.text.toString()
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

    override fun handleBack() {
        findNavController().navigate(R.id.action_notifications_to_topNews)
    }
}