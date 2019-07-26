package com.mathgeniusguide.project7.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.util.Constants
import com.mathgeniusguide.project7.util.OnSwipeTouchListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.news.*

private const val ARG_PARAM1 = "param1"

class News: Fragment() {
    private var param1: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }

        val title = when (param1){
            1 -> Constants.TAB_1_TITLE
            2 -> Constants.TAB_2_TITLE
            else -> Constants.TAB_0_TITLE
        }
        activity?.setTitle(resources.getString(title))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.news, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSwipeListener(view)
        news_test.text = "Tab ${param1} News."

        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selected = tab!!.position
                Toast.makeText(context, "Tab ${selected} selected.", Toast.LENGTH_LONG).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun setSwipeListener(view: View) {
        context?.let {
            view.setOnTouchListener(object : OnSwipeTouchListener(it) {

            })
        }
    }
}