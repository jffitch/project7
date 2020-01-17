package com.mathgeniusguide.project7.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.connectivity.convertDate
import com.mathgeniusguide.project7.responses.search.SearchResult
import com.mathgeniusguide.project7.util.Constants
import kotlinx.android.synthetic.main.news_item.view.*
import java.util.*

class SearchAdapter(
    private val items: ArrayList<SearchResult>,
    val context: Context,
    val navController: NavController
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
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
        holder.displayDate.text = pos.pub_date.convertDate()
        // set category to fetched section and subsection
        val stringResource =
            if (pos.subsection_name != null && pos.subsection_name.isNotBlank()) R.string.section_and_subsection else R.string.section_only
        holder.displayCategory.text = String.format(
            context.resources.getString(stringResource),
            pos.section_name,
            pos.subsection_name
        )
        // load fetched image into ImageView using Glide
        if (pos.multimedia != null && pos.multimedia.isNotEmpty()) {
            Glide.with(context).load("https://nytimes.com/${pos.multimedia[0].url}")
                .into(holder.displayImage)
        }
        // open webpage on click
        holder.parent.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", pos.web_url)
            // save current fragment in bundle for when back arrow is pressed
            bundle.putInt("previous", Constants.SEARCH_NEWS)
            navController.navigate(R.id.action_to_webpage, bundle)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // variables here are used in onBindViewHolder
        val displayDate = view.displayDate
        val displayTitle = view.displayTitle
        val displayCategory = view.displayCategory
        val displayImage = view.displayImage
        val parent = view
    }
}