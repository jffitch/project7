package com.mathgeniusguide.project7.connectivity

import org.junit.Test

class NoConnectivityExceptionTest {
    @Test
    fun connectivityExceptionMessageCorrect() {
        val exception = NoConnectivityException()
        assert(exception.localizedMessage == "No connectivity Exception")
    }
}