package com.mathgeniusguide.project7.base

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            handleBack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    abstract fun handleBack()
}