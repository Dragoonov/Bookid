package com.moonlightbutterfly.bookid

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moonlightbutterfly.bookid.repository.database.entities.User

class ManagerFake : Manager {
    private val _user = MutableLiveData<User>()
    override val user: LiveData<User?>
        get() = _user

    override fun singOutUser(context: Context) {
        _user.value = null
    }

    override fun signInUser(user: User) {
        _user.value = user
    }
}