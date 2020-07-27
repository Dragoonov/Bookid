package com.moonlightbutterfly.bookid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunicatorImpl @Inject constructor(): Communicator {
    override val message: LiveData<String> get() = _message
    private var _message: MutableLiveData<String> = MutableLiveData()

    override fun postMessage(message: String) = run { _message.value = message }
    override fun clearMessage() {
        _message.value = null
    }
}