package com.moonlightbutterfly.bookid.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.moonlightbutterfly.bookid.DrawerManager
import com.moonlightbutterfly.bookid.MainActivity
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.databinding.FragmentLoginBinding
import javax.inject.Inject


class LoginFragment : BaseFragment<FragmentLoginBinding, ViewModel>() {

    @Inject
    lateinit var userManager: UserManager

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentLoginBinding.inflate(inflater, container, false).also {
            it.fragment = this
            it.signInButton.apply {
                setSize(SignInButton.SIZE_STANDARD)
                setOnClickListener { signIn() }
            }
        }
    }

    override fun initializeCustom() {
        initializeGoogleSignIn()
        (activity as DrawerManager).lockDrawer()
    }

    private fun initializeGoogleSignIn() {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(activity as AppCompatActivity, gso)
    }

    private fun signIn() = activity
        ?.startActivityForResult(googleSignInClient.signInIntent, MainActivity.SIGN_IN_CODE)

}