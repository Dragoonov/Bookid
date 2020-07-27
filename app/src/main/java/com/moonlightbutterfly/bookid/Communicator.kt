package com.moonlightbutterfly.bookid

import androidx.lifecycle.LiveData

interface Communicator {
    val message: LiveData<String>
    fun postMessage(message: String)
    fun clearMessage()
}
