package com.moonlightbutterfly.bookid

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val internalRepository: InternalRepository,
    private val communicator: Communicator
) : Manager {
    override val user: LiveData<User?> get() = _user


    override fun isUserSignedIn(): Boolean = userSignedIn

    private val _user: MutableLiveData<User?> = liveData {
        internalRepository.getUser().collect { data -> emit(data) }
    } as MutableLiveData<User?>

    private var userSignedIn = false

    override fun singOutUser(context: Context) {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        GoogleSignIn.getClient(context as AppCompatActivity, gso).signOut()
        if (user.value != null) {
            GlobalScope.launch {
                internalRepository.deleteUser(user.value!!)
            }
        }
        communicator.postMessage(context.getString(R.string.signed_out))
        userSignedIn = false
    }

    override fun signInUser(user: User): Unit = run {
        GlobalScope.launch {
            internalRepository.insertUser(user)
        }
        userSignedIn = true
    }
}
