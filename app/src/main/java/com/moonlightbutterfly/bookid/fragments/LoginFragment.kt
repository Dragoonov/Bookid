package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.MainActivity
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.databinding.FragmentLoginBinding
import javax.inject.Inject


class LoginFragment : Fragment() {

    companion object {
        fun newInstance(): LoginFragment =
            LoginFragment()
    }

    @Inject
    lateinit var userManager: UserManager

    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        val binding: FragmentLoginBinding =
            FragmentLoginBinding.inflate(layoutInflater, container, false)
        binding.fragment = this
        binding.signInButton.apply {
            setSize(SignInButton.SIZE_STANDARD)
            setOnClickListener { signIn() }
        }
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(activity as AppCompatActivity, gso)

        userManager.getUserFromDatabase().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                userManager.loggedUser = it
                //TODO Launch SearchFragment
                Log.v("LoginFragment", "Zalogowano jako $it")
            } else {
                binding.signInButton.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        activity?.startActivityForResult(signInIntent, MainActivity.SIGN_IN_CODE)
    }

}