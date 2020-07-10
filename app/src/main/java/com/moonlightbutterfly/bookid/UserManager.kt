package com.moonlightbutterfly.bookid

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val internalRepository: InternalRepository) {

    val user: LiveData<User> get() = _user
    private val _user: MutableLiveData<User> = Transformations.switchMap(internalRepository.getLoggedUser()) {
        MutableLiveData(it)
    } as MutableLiveData<User>

    fun singOutUser(context: Context) {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        GoogleSignIn.getClient(context as AppCompatActivity, gso).signOut()
        if (user.value != null) {
            internalRepository.deleteLoggedUser(user.value!!)
            _user.value = null
        }
    }
    fun signInUser(user: User) = internalRepository.insertLoggedUser(user)
}
