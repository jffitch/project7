package com.mathgeniusguide.project7.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.util.Constants
import com.mathgeniusguide.project7.util.OnSwipeTouchListener
import com.mathgeniusguide.project7.viewmodel.NewsViewModel

class News: Fragment() {
    lateinit var viewModel: NewsViewModel
    var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("position")
        }
        viewModel = NewsViewModel(activity!!.application)
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

        fetch(position)
    }

    fun fetch(n: Int) {
        when (n) {
            Constants.TOP_NEWS -> {
                viewModel.fetchTopNews()
            }
            Constants.POPULAR_NEWS -> {
                viewModel.fetchPopularNews()
            }
            Constants.POLITICS_NEWS -> {
                viewModel.fetchPoliticsNews()
            }
        }
    }

    private fun setSwipeListener(view: View) {
        context?.let {
            view.setOnTouchListener(object : OnSwipeTouchListener(it) {

            })
        }
    }
}