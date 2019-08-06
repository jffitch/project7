package com.mathgeniusguide.project7.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.responses.popular.PopularResult
import kotlinx.android.synthetic.main.news_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class PopularAdapter (private val items: ArrayList<PopularResult>, val context: Context, val rv: RecyclerView, val wv: WebView, val back: ImageView) : RecyclerView.Adapter<PopularAdapter.ViewHolder> () {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // first argument in "inflate" is the layout for the group
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // code here controls what's done to the views for each group
        val pos = items[position]
        holder.displayTitle.text = pos.title
        holder.displayDate.text = convertDate(pos.published_date)
        holder.displayCategory.text = pos.section
        if (pos.media.size != 0 && pos.media[0].mediaMetadata.size != 0) {
            Glide.with(context).load(pos.media[0].mediaMetadata[0].url).into(holder.displayImage)
        }
        holder.parent.setOnClickListener {
            wv.loadUrl(pos.url)
            wv.settings.javaScriptEnabled = true
            wv.visibility = View.VISIBLE
            rv.visibility = View.GONE
            back.visibility = View.VISIBLE
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
        val parent = view.parentView
    }
}