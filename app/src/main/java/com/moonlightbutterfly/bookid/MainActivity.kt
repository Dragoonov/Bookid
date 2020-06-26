package com.moonlightbutterfly.bookid


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.moonlightbutterfly.bookid.fragments.*
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.database.entities.User
import javax.inject.Inject


enum class TEMP {
    PROFILE, SHELF, BOOK, SEARCH, EDIT, LOGIN
}


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userManager: UserManager

    companion object {
        const val SIGN_IN_CODE = 100
    }

//    private val booksList = ArrayList<Book>().apply {
//        add(
//            Book(
//                3,
//                "title1", Author(1, "author1", "imageurl"), "pubdate", 1.2, "image"
//            )
//        )
//        add(
//            Book(
//                3,
//                "title2", Author(2, "author2", "imageurl"), "pubdate", 1.2, "image"
//            )
//        )
//        add(
//            Book(
//                3,
//                "title3", Author(3, "author3", "imageurl"), "pubdate", 1.2, "image"
//            )
//        )
//    }
//    val shelfListMock = ArrayList<Shelf>().apply {
//        add(
//            Shelf(
//                1,
//                "Polka1",
//                booksList,
//                1
//            )
//        )
//        add(
//            Shelf(
//                2,
//                "Polka2",
//                booksList,
//                1
//            )
//        )
//        add(
//            Shelf(
//                3,
//                "Polka3",
//                booksList,
//                1
//            )
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BookidApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userManager.getUserFromDatabase().observe(this, Observer {
            userManager.loggedUser = it
        })

        val temp = TEMP.SEARCH

        if (savedInstanceState == null && temp == TEMP.PROFILE) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment.newInstance())
                .commit()
        }

        if (savedInstanceState == null && temp == TEMP.SHELF) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ShelfFragment.newInstance())
                .commit()
        }
        if (savedInstanceState == null && temp == TEMP.SEARCH) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, SearchFragment.newInstance())
                .commit()
        }

        if (savedInstanceState == null && temp == TEMP.EDIT) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, EditShelfsFragment.newInstance())
                .commit()
        }

        if (savedInstanceState == null && temp == TEMP.LOGIN) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, LoginFragment.newInstance())
                .commit()
        }

        if (savedInstanceState == null && temp == TEMP.BOOK) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container, BookFragment.newInstance(
                        Book(
                            1,
                            "Title 1",
                            Author(18541, "pupa", "il"),
                            "33",
                            4.5,
                            "345"
                        )
                    )
                )
                .commit()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                val loggedUser = User(
                    account.id!!,
                    account.displayName,
                    account.email,
                    account.photoUrl.toString())
                userManager.loggedUser = loggedUser
                userManager.saveUserToDatabase(loggedUser)
                //TODO Launch SearchFragment
                Log.v("MainActivity", "Zalogowano jako $loggedUser")
            } else {
                Toast.makeText(this, R.string.login_fail, Toast.LENGTH_LONG).show()
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("MainActivity", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, R.string.login_fail, Toast.LENGTH_LONG).show()
        }
    }
}
