package com.moonlightbutterfly.bookid

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val internalRepository: InternalRepository,
    private val communicator: Communicator,
    private val schedulerProvider: SchedulerProvider
) : Manager {

    private var userId: MutableLiveData<String> = MutableLiveData()

    override val user: LiveData<User> get() = _user

    private val disposable = CompositeDisposable()

    private val _user: MutableLiveData<User> = userId.switchMap {
        LiveDataReactiveStreams.fromPublisher(
            internalRepository.getUserById(userId.value!!)
                .map {
                    if (it.isEmpty()) {
                        User("")
                    } else {
                        it[0]
                    }
                }
        )
    } as MutableLiveData<User>

    override fun receiveUserId(id: String) {
        userId.value = id
    }

    override fun signOutUser(context: Context) {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        GoogleSignIn.getClient(context as AppCompatActivity, gso).signOut()
        userId.value = ""
        context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE).edit().putString(ID_KEY, "").apply()
        communicator.postMessage(context.getString(R.string.signed_out))
    }

    override fun signInUser(user: User, context: Context) {
        disposable.add(
            internalRepository.getUserById(user.id)
                .take(1)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe {
                if (it.isEmpty()) {
                    disposable.add(internalRepository
                        .insertUser(user)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe())
                }
                userId.value = user.id
            }
        )
        context.getSharedPreferences (FILE_KEY, Context.MODE_PRIVATE).edit().putString(ID_KEY, user.id).apply()
    }

    override fun updateBaseShelf(shelfId: Int): Unit = run {
        val userTemp = user.value
        userTemp?.baseShelfId = shelfId
        disposable.add(internalRepository.updateUser(userTemp!!)
            .subscribeOn(schedulerProvider.io())
            .subscribe())
    }

    companion object {
        const val ID_KEY = "user_id"
        const val FILE_KEY = "user_file"
    }
}
