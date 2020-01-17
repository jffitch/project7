package com.mathgeniusguide.project7.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mathgeniusguide.project7.MainActivity
import com.mathgeniusguide.project7.R
import com.mathgeniusguide.project7.base.BaseFragment
import com.mathgeniusguide.project7.util.Constants
import kotlinx.android.synthetic.main.web_fragment.*

class WebFragment : BaseFragment() {
    // initialize variables for url and previous fragment, which will later be gotten from bundle
    var url = ""
    var previous = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.web_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavigationView()
        (activity as? MainActivity)?.showActionBar()
    }

    // hide warning for enabling JavaScript
    @SuppressLint("SetJavaScriptEnabled")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // get variables from bundle
        arguments?.let {
            url = it.getString("url", "")
            previous = it.getInt("previous", 0)
        }
        // set loaded url and enable JavaScript
        webView.loadUrl(url)
        webView.settings.javaScriptEnabled = true
    }

    override fun handleBack() {
        // go back to previous fragment, using "previous" variable
        findNavController().navigate(
            when (previous) {
                Constants.POPULAR_NEWS -> R.id.action_web_to_mostPopular
                Constants.POLITICS_NEWS -> R.id.action_web_to_politicsNews
                Constants.SEARCH_NEWS -> R.id.action_web_to_searchResult
                else -> R.id.action_web_to_topNews
            }
        )
    }
}