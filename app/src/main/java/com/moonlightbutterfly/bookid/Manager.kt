package com.moonlightbutterfly.bookid

import android.content.Context
import androidx.lifecycle.LiveData
import com.moonlightbutterfly.bookid.repository.database.entities.User

interface Manager {
    val user: LiveData<User>
    fun signOutUser(context: Context)
    fun signInUser(user: User, context: Context)
    fun updateBaseShelf(shelfId: Int)
    fun receiveUserId(id: String)
}
