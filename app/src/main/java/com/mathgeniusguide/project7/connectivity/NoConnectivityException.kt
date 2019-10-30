package com.mathgeniusguide.project7.connectivity

import java.io.IOException

class NoConnectivityException : IOException() {

    override val message: String?
        get() = "No Connectivity Exception"

    override fun getLocalizedMessage(): String? {
        return "No connectivity Exception"
    }
}