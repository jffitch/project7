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
import com.mathgeniusguide.project7.responses.category.CategoryResult
import kotlinx.android.synthetic.main.news_item.view.*
import java.util.*

// include views as arguments so their visibility can be changed in the onClick functions
class CategoryAdapter (private val items: ArrayList<CategoryResult>, val context: Context, private val navController: NavController, private val tabPosition: Int) : RecyclerView.Adapter<CategoryAdapter.ViewHolder> () {
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
        holder.displayDate.text = pos.created_date.convertDate()
        // set category to fetched section and subsection
        val stringResource = if (pos.subsection != null && pos.subsection.isNotBlank()) R.string.section_and_subsection else R.string.section_only
        holder.displayCategory.text = String.format(context.resources.getString(stringResource), pos.section, pos.subsection)
        // load fetched image into ImageView using Glide
        if (pos.multimedia.isNotEmpty()) {
            Glide.with(context).load(pos.multimedia[0].url).into(holder.displayImage)
        }
        holder.parent.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", pos.url)
            bundle.putInt("previous", tabPosition)
            navController.navigate(R.id.action_to_webpage, bundle)
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