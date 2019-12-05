package com.mathgeniusguide.project7.connectivity

import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat
import java.util.*

fun Context.isOnline() : Boolean {
    val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = connectivityManager.activeNetworkInfo
    return info != null && info.isConnected
}

fun String.convertDate(): String {
    val toDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = toDate.parse(this)
    val toString = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
    return toString.format(date) + "XXX"
}