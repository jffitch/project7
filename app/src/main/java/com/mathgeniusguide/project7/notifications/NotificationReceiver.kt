package com.mathgeniusguide.project7.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mathgeniusguide.project7.MainActivity
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.adapter.SearchAdapter
import com.mathgeniusguide.project7.api.ApiFactory
import com.mathgeniusguide.project7.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.search.*
import java.text.SimpleDateFormat
import java.util.*

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = NotificationManagerCompat.from(context!!)
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)
        val searchTerm = intent?.getStringExtra("searchTerm")
        val categories = intent?.getStringExtra("categories")

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Date()
        val dateBegin = sdf.format(Date(today.time - 86400000)) + "T00:00:00Z"
        val dateEnd = sdf.format(today) + "T23:59:59Z"

        val viewModel = NewsViewModel(context.applicationContext as App)
        viewModel.fetchSearchNews(searchTerm!!, categories!!, dateBegin, dateEnd)
    }
}