package com.moonlightbutterfly.bookid


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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
    PROFILE, SHELF, BOOK, SEARCH, EDIT, LOGIN, SETTINGS, BLANK
}


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userManager: UserManager

    lateinit var navController: NavController

    companion object {
        const val SIGN_IN_CODE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BookidApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)

//        val temp = TEMP.BLANK
//
//        userManager.getUserFromDatabase().observe(this, Observer {
//            userManager.loggedUser = it ?: User(
//                id="105158656156171336148",
//                nick="Jakub Lipowski",
//                email="dragovonnova@gmail.com",
//                avatar="https://lh3.googleusercontent.com/a-/AOh14GiwnAPQePJ69JG078iyC11Q3pNuChevnigQ9Xy5")
//
//            if (savedInstanceState == null && temp == TEMP.PROFILE) {
//                navController.navigate(R.id.action_global_profileFragment)
//            }
//
//            if (savedInstanceState == null && temp == TEMP.SHELF) {
//                navController.navigate(R.id.action_global_shelfFragment)
//            }
//
//            if (savedInstanceState == null && temp == TEMP.SEARCH) {
//                navController.navigate(R.id.action_global_searchFragment)
//            }
//
//            if (savedInstanceState == null && temp == TEMP.EDIT) {
//                navController.navigate(R.id.action_global_editShelfsFragment)
//            }
//
//            if (savedInstanceState == null && temp == TEMP.LOGIN) {
//                navController.navigate(R.id.login_graph)
//            }
//
//            if (savedInstanceState == null && temp == TEMP.SETTINGS) {
//                navController.navigate(R.id.action_global_settingsFragment)
//            }
//
//            if (savedInstanceState == null && temp == TEMP.BOOK) {
//                val book = Book(
//                    1,
//                    "Title 1",
//                    Author(18541, "pupa", "il"),
//                    "33",
//                    4.5,
//                    "345"
//                )
//                val action = NavGraphDirections.actionGlobalBookFragment(Converters.convertToJSONString(book))
//                navController.navigate(action)
//            }
//        })
//

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
                navController.navigate(R.id.action_global_searchFragment)
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
