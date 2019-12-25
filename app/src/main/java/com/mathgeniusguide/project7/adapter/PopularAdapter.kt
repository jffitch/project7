package com.mathgeniusguide.project7.adapter

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
import com.mathgeniusguide.project7.responses.popular.PopularResult
import com.mathgeniusguide.project7.util.Constants
import kotlinx.android.synthetic.main.news_item.view.*
import java.util.*

class PopularAdapter (private val items: ArrayList<PopularResult>, val context: Context, private val navController: NavController) : RecyclerView.Adapter<PopularAdapter.ViewHolder> () {
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
        // set title to fetched title
        holder.displayTitle.text = pos.title
        // set date to fetched date, converted to "MMMM d, yyyy" format
        holder.displayDate.text = pos.published_date.convertDate()
        // set category to fetched section
        holder.displayCategory.text = pos.section
        // load fetched image into ImageView using Glide
        if (pos.media.isNotEmpty() && pos.media[0].mediaMetadata.isNotEmpty()) {
            Glide.with(context).load(pos.media[0].mediaMetadata[0].url).into(holder.displayImage)
        }
        holder.parent.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", pos.url)
            bundle.putInt("previous", Constants.POPULAR_NEWS)
            navController.navigate(R.id.action_to_webpage, bundle)
        }
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