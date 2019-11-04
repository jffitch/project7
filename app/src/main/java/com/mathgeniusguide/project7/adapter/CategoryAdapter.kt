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
import com.mathgeniusguide.project7.MainActivity
import com.mathgeniusguide.project7.responses.category.CategoryResult
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.connectivity.convertDate
import kotlinx.android.synthetic.main.news_item.view.*
import java.text.SimpleDateFormat
import java.util.*

// include views as arguments so their visibility can be changed in the onClick functions
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
        // set title to fetched title
        holder.displayTitle.text = pos.title
        // set date to fetched date, converted to "MMMM d, yyyy" format
        holder.displayDate.text = pos.created_date.convertDate()
        // set category to fetched section and subsection
        holder.displayCategory.text = "${pos.section}${if (pos.subsection != null && pos.subsection.isNotBlank()) "> ${pos.subsection}" else ""}"
        // load fetched image into ImageView using Glide
        if (pos.multimedia.size != 0) {
            Glide.with(context).load(pos.multimedia[0].url).into(holder.displayImage)
        }
        holder.parent.setOnClickListener {
            // load fetched url into WebView
            wv.loadUrl(pos.url)
            wv.settings.javaScriptEnabled = true
            // WebView becomes visible to view webpage
            wv.visibility = View.VISIBLE
            rv.visibility = View.GONE
            back.visibility = View.VISIBLE
        }
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