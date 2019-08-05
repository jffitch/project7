package com.mathgeniusguide.project7.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.responses.popular.PopularResult
import kotlinx.android.synthetic.main.news_item.view.*

class PopularAdapter (private val items: ArrayList<PopularResult>, val context: Context) : RecyclerView.Adapter<PopularAdapter.ViewHolder> () {
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
        holder.displayDate.text = pos.published_date
        holder.displayCategory.text = pos.section
    }

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        // variables here are used in onBindViewHolder
        val displayDate = view.displayDate
        val displayTitle = view.displayTitle
        val displayCategory = view.displayCategory
    }
}