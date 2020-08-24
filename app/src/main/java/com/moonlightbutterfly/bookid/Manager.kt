package com.moonlightbutterfly.bookid

import android.content.Context
import androidx.lifecycle.LiveData
import com.moonlightbutterfly.bookid.repository.database.entities.User

interface Manager {
    val user: LiveData<User?>
    fun isUserSignedIn(): Boolean
    fun singOutUser(context: Context)
    fun signInUser(user: User)
}
