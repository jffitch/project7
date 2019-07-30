package com.mathgeniusguide.project7.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.util.OnSwipeTouchListener
import kotlinx.android.synthetic.main.search.*

class Search: Fragment() {
    var screen: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            screen = it.getInt("screen")
        }
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
        search_test.text = if (screen == 0) "Search" else "Notifications"
    }

    private fun setSwipeListener(view: View) {
        context?.let {
            view.setOnTouchListener(object : OnSwipeTouchListener(it) {

            })
        }
    }
}