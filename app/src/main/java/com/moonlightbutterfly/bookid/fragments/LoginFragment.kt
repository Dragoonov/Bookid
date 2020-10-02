package com.moonlightbutterfly.bookid.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.transition.TransitionInflater
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.moonlightbutterfly.bookid.MainActivity
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.FragmentLoginBinding
import com.moonlightbutterfly.bookid.getNavController
import com.moonlightbutterfly.bookid.utils.EnhancedAccelerateDecelerateInterpolator
import javax.inject.Inject


class LoginFragment : BaseFragment<FragmentLoginBinding, ViewModel>() {

    private var finishedAnimation = false

    private val animationListener = object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            binding?.signInButton?.visibility = View.VISIBLE
        }

        override fun onAnimationEnd(animation: Animator?) {
            finishedAnimation = true
        }
    }

    @Inject
    lateinit var userManager: Manager

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentLoginBinding.inflate(inflater, container, false).also {
            it.signInButton.apply {
                setSize(SignInButton.SIZE_STANDARD)
                setOnClickListener { signIn() }
            }
        }
    }

    override fun initializeCustom(savedInstanceState: Bundle?) {
        savedInstanceState?.let { finishedAnimation = savedInstanceState.getBoolean(::finishedAnimation.name) }
        initializeGoogleSignIn()
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.change_bounds).apply {
            duration = 1000
            interpolator = EnhancedAccelerateDecelerateInterpolator()
        }
        binding?.signInButton?.animateIfNeeded()
        requireContext().getNavController().lockBottomNav()
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(::finishedAnimation.name, finishedAnimation)
        super.onSaveInstanceState(outState)
    }

    private fun View.animateIfNeeded(): Unit =
        if (finishedAnimation) {
            alpha = 1f
            visibility = View.VISIBLE
        } else {
            animate().apply {
                duration = 500
                startDelay = 1100
                alpha(1f)
                setListener(animationListener)
                start()
            }
            Unit
        }

}