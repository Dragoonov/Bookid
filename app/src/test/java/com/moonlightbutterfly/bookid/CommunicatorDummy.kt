package com.moonlightbutterfly.bookid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CommunicatorDummy: Communicator {
    override val message: LiveData<String> = MutableLiveData()

    override fun postMessage(message: String) {
    }

    override fun clearMessage() {
    }
}