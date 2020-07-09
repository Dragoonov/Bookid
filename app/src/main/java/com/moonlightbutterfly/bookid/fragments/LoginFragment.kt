package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.moonlightbutterfly.bookid.*
import com.moonlightbutterfly.bookid.databinding.FragmentLoginBinding
import javax.inject.Inject


class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null

    @Inject
    lateinit var userManager: UserManager

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false).also {
            it.fragment = this
            it.signInButton.apply {
                setSize(SignInButton.SIZE_STANDARD)
                setOnClickListener { signIn() }
            }
        }
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(activity as AppCompatActivity, gso)
        (activity as DrawerManager).lockDrawer()
        return binding?.root
    }

    private fun signIn() = activity
        ?.startActivityForResult(googleSignInClient.signInIntent, MainActivity.SIGN_IN_CODE)

}