package com.mathgeniusguide.project7.fragments


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mathgeniusguide.project7.R

class Splash : Fragment() {
    private var searchTerm = ""
    private var categories = ""
    private var dateBegin = ""
    private var dateEnd = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = activity?.intent?.extras
        if (bundle != null) {
            searchTerm = bundle.getString("searchTerm", "")
            categories = bundle.getString("categories", "")
            dateBegin = bundle.getString("dateBegin", "")
            dateEnd = bundle.getString("dateEnd", "")
        }

        navigateAfterDelay(searchTerm != "")
    }

    private fun navigateAfterDelay(loaded: Boolean) {
        Handler().postDelayed({
            moveToAppropriateFragment(loaded)
        }, 1000)
    }

    private fun moveToAppropriateFragment(loaded: Boolean) {
        if (loaded) {
            val bundle = Bundle()
            bundle.putString("searchTerm", searchTerm)
            bundle.putString("categories", categories)
            bundle.putString("dateBegin", dateBegin)
            bundle.putString("dateEnd", dateEnd)
            findNavController().navigate(R.id.load_notification, bundle)
        } else {
            findNavController().navigate(R.id.load_normal)
        }
    }
}