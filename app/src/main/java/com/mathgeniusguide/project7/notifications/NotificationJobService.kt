package com.mathgeniusguide.project7.notifications

import android.app.Notification
import android.app.PendingIntent
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

import androidx.core.app.NotificationManagerCompat;
import com.mathgeniusguide.project7.MainActivity
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.api.ApiFactory
import com.mathgeniusguide.project7.responses.search.SearchResponseFull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/** Job service to show notifications once a day **/
class NotificationJobService : JobService() {
    private lateinit var notificationManagerCompat: NotificationManagerCompat
    private var newsCount = 0
    private var searchTerm = ""
    private var categories = ""
    private var dateBegin = ""
    private var dateEnd = ""
    private var notificationSent = false

    override fun onStartJob(params: JobParameters): Boolean {
        // get Search Term and Categories from bundle
        searchTerm = params.extras.getString("searchTerm")
        categories = params.extras.getString("categories")
        // set date range to between yesterday at 0:00:00 and today at 23:59:59
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Date()
        dateBegin = sdf.format(today) + "T00:00:00Z"
        dateEnd = sdf.format(Date(today.time + 86400000)) + "T23:59:59Z"
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext())

        searchForNews()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    private fun searchForNews() {
        val searchNewsNotSuspended =
            ApiFactory.api.getSearchNewsNotSuspended(searchTerm, categories, dateBegin, dateEnd)

        searchNewsNotSuspended.enqueue(object : Callback<SearchResponseFull> {
            override fun onResponse(call: Call<SearchResponseFull>, response: Response<SearchResponseFull>) {
                if (response.isSuccessful) {
                    val newsCount = response.body()?.response?.docs?.size ?: 0
                    if (newsCount > 0) {
                        sendNotification(newsCount, searchTerm, categories, dateBegin, dateEnd)
                        val list = response.body()?.response?.docs
                        for (item in list!!) {
                            Log.d("NotificationJobService", item.headline.main)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponseFull>, t: Throwable) {

            }
        })
    }

    private fun sendNotification(newsCount: Int, searchTerm: String, categories: String, dateBegin: String, dateEnd: String) {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val title = "News Found"
        val message = "${newsCount} new ${categories} items involving ${searchTerm}."

        val notification = NotificationCompat.Builder(applicationContext, "notificationChannel")
            .setSmallIcon(R.drawable.image_placeholder)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, notification)
    }
}