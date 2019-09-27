package com.mathgeniusguide.project7.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.responses.search.SearchResult
import kotlinx.android.synthetic.main.news_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class SearchAdapter (private val items: ArrayList<SearchResult>, val context: Context, val rv: RecyclerView, val wv: WebView, val back: ImageView) : RecyclerView.Adapter<SearchAdapter.ViewHolder> () {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // first argument in "inflate" is the layout for the group
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false))
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // code here controls what's done to the views for each group
        val pos = items[position]
        // set title to fetched headline
        holder.displayTitle.text = pos.headline.print_headline
        // set date to fetched date, converted to "MMMM d, yyyy" format
        holder.displayDate.text = convertDate(pos.pub_date)
        // set category to fetched section and subsection
        holder.displayCategory.text = "${pos.section_name}${if (pos.subsection_name != null && pos.subsection_name.isNotBlank()) "> ${pos.subsection_name}" else ""}"
        // load fetched image into ImageView using Glide
        if (pos.multimedia.size != 0) {
            Glide.with(context).load("https://nytimes.com/${pos.multimedia[0].url}").into(holder.displayImage)
        }
        holder.parent.setOnClickListener {
            // load fetched url into WebView
            wv.loadUrl(pos.web_url)
            wv.settings.javaScriptEnabled = true
            // WebView becomes visible to view webpage
            wv.visibility = View.VISIBLE
            back.visibility = View.VISIBLE
            rv.visibility = View.GONE
        }
    }

    fun convertDate(string: String): String {
        val toDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = toDate.parse(string)
        val toString = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        return toString.format(date)
    }

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        // variables here are used in onBindViewHolder
        val displayDate = view.displayDate
        val displayTitle = view.displayTitle
        val displayCategory = view.displayCategory
        val displayImage = view.displayImage
        val parent = view
    }
}