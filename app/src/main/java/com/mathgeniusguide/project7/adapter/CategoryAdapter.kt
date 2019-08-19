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
import com.mathgeniusguide.project7.responses.category.CategoryResult
import com.mathgeniusguide.project7.R
import kotlinx.android.synthetic.main.news_item.view.*
import kotlinx.android.synthetic.main.news.*
import java.text.SimpleDateFormat
import java.util.*

class CategoryAdapter (private val items: ArrayList<CategoryResult>, val context: Context, val rv: RecyclerView, val wv: WebView, val back: ImageView) : RecyclerView.Adapter<CategoryAdapter.ViewHolder> () {
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
        holder.displayTitle.text = pos.title
        holder.displayDate.text = convertDate(pos.created_date)
        holder.displayCategory.text = "${pos.section} > ${pos.subsection}"
        if (pos.multimedia.size != 0) {
            Glide.with(context).load(pos.multimedia[0].url).into(holder.displayImage)
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
        val parent = view
    }
}