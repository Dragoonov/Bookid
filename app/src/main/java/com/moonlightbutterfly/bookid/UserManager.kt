package com.moonlightbutterfly.bookid

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val internalRepository: InternalRepository,
    private val communicator: Communicator
) : Manager {

    private var userId: MutableLiveData<String> = MutableLiveData()

    override val user: LiveData<User?> get() = _user

    private val _user: MutableLiveData<User?> = Transformations.switchMap(userId) {
        liveData {
            internalRepository.getUserById(userId.value!!)?.collect { data -> emit(data) }
        }
    } as MutableLiveData<User?>

    override fun provideUserId(id: String) {
        userId.value = id
    }

    override fun singOutUser(context: Context) {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        GoogleSignIn.getClient(context as AppCompatActivity, gso).signOut()
        userId.value = ""
        context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE).edit().putString(ID_KEY, "").apply()
        communicator.postMessage(context.getString(R.string.signed_out))
    }

    override fun signInUser(user: User, context: Context): Unit = run {
        GlobalScope.launch(Dispatchers.Main) {
            internalRepository.getUserById(user.id)?.collect {
                if (it == null) {
                    internalRepository.insertUser(user)
                }
                userId.value = user.id
                cancel()
            }
        }
        context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE).edit().putString(ID_KEY, user.id).apply()
    }

    override fun updateBaseShelf(shelfId: Int): Unit = run {
        GlobalScope.launch {
            val userTemp = user.value
            userTemp?.baseShelfId = shelfId
            internalRepository.updateUser(userTemp!!)
        }
    }

    companion object {
        const val ID_KEY = "user_id"
        const val FILE_KEY = "user_file"
    }
}
