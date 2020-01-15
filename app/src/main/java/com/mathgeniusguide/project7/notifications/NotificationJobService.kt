package com.mathgeniusguide.project7.notifications

import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

import androidx.core.app.NotificationManagerCompat
import com.mathgeniusguide.project7.MainActivity
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.api.Api
import com.mathgeniusguide.project7.connectivity.ConnectivityInterceptor
import com.mathgeniusguide.project7.connectivity.NoConnectivityException
import com.mathgeniusguide.project7.responses.search.SearchResponseFull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/** Job service to show notifications once a day **/
class NotificationJobService : JobService() {
    private lateinit var notificationManagerCompat: NotificationManagerCompat
    // initialize notification variables
    private var searchTerm = ""
    private var categories = ""
    private var dateBegin = ""
    private var dateEnd = ""

    override fun onStartJob(params: JobParameters): Boolean {
        // get SearchFragment Term and Categories from bundle
        searchTerm = params.extras.getString("searchTerm") ?: ""
        categories = params.extras.getString("categories") ?: ""
        // set date range to between yesterday at 0:00:00 and today at 23:59:59
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Date()
        dateBegin = sdf.format(today) + "T00:00:00Z"
        dateEnd = sdf.format(Date(today.time + 86400000)) + "T23:59:59Z"
        notificationManagerCompat = NotificationManagerCompat.from(applicationContext)

        searchForNews()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    private fun searchForNews() {
        val connectivityInterceptor = ConnectivityInterceptor(applicationContext)

        try {
            // fetch from API using searchTerm, categories, dateBegin, dateEnd to get number of news items
            val searchNewsNotSuspended =
                Api.invoke(connectivityInterceptor)
                    .getSearchNewsNotSuspended(searchTerm, categories, dateBegin, dateEnd)
            searchNewsNotSuspended.enqueue(object : Callback<SearchResponseFull> {
                override fun onResponse(
                    call: Call<SearchResponseFull>,
                    response: Response<SearchResponseFull>
                ) {
                    if (response.isSuccessful) {
                        // if there are news items, send notification
                        val newsCount = response.body()?.response?.docs?.size ?: 0
                        if (newsCount > 0) {
                            // send news count to display in notification, and API call variables to run API call again when notification is clicked
                            sendNotification(newsCount, searchTerm, categories, dateBegin, dateEnd)
                            val list = response.body()?.response?.docs
                            // show news items in log to confirm that news is loaded correctly
                            for (item in list!!) {
                                Log.d("NotificationJobService", item.headline.main)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<SearchResponseFull>, t: Throwable) {

                }
            })

        } catch (e: NoConnectivityException) {
            Log.e("NotificationJobService", e.localizedMessage)
        }
    }

    private fun sendNotification(
        newsCount: Int,
        searchTerm: String,
        categories: String,
        dateBegin: String,
        dateEnd: String
    ) {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        // create intent, bundle with variables, notification title, and notification message
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("searchTerm", searchTerm)
        intent.putExtra("categories", categories)
        intent.putExtra("dateBegin", dateBegin)
        intent.putExtra("dateEnd", dateEnd)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val title = resources.getString(R.string.notification_title)
        val message = String.format(resources.getString(R.string.notification_message), newsCount, categories, searchTerm)

        // create and send notification
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