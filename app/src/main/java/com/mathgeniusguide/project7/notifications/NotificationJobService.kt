package com.mathgeniusguide.project7.notifications

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.mathgeniusguide.project7.MainActivity
import com.mathgeniusguide.project7.R



/** Job service to show notifications once a day **/
class NotificationJobService : JobService() {
    private lateinit var notificationManagerCompat: NotificationManagerCompat
    private var newsCount = 0
    private var searchTerm = ""
    private var categories = ""
    private var dateBegin = ""
    private var dateEnd = ""

    override fun onStartJob(params: JobParameters): Boolean {
        newsCount = params.extras.getInt("newsCount")
        searchTerm = params.extras.getString("searchTerm")
        categories = params.extras.getString("categories")
        dateBegin = params.extras.getString("dateBegin")
        dateEnd = params.extras.getString("dateEnd")
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext())

        Thread {
            if (newsCount != 0)
                checkForNotifications()
        }.start()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    private fun checkForNotifications() {
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText("Tap to go to the articles")
            .setBigContentTitle("There is ${newsCount} news about ${searchTerm} to read today!")
            .setSummaryText("Your daily news report")
        val notifyIntent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString("searchTerm", searchTerm)
        bundle.putString("categories", categories)
        bundle.putString("dateBegin", dateBegin)
        bundle.putString("dateEnd", dateEnd)
        notifyIntent.putExtras(bundle)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val dismissIntent = Intent(this, BigTextIntentService::class.java)
        dismissIntent.action = BigTextIntentService.ACTION_DISMISS
        val dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, 0)
        val dismissAction = NotificationCompat.Action.Builder(R.drawable.search, "Dismiss", dismissPendingIntent).build()
        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext, "channel1")
        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder)
        var notification: Notification? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notification = notificationCompatBuilder.setStyle(bigTextStyle)
                .setContentTitle("There is ${newsCount} news about ${searchTerm} to read today!")
                .setContentText("NewsReader")
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentIntent(notifyPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                .setCategory(Notification.CATEGORY_REMINDER)
                .addAction(dismissAction)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()
        }
        notificationManagerCompat.notify(1, notification!!)
    }
}