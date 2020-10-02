package com.moonlightbutterfly.bookid.fragments

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.ProfileFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.ProfileViewModel

class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>(ProfileViewModel::class.java) {

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = ProfileFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.textLink.apply {
                val policy = HtmlCompat.fromHtml(
                    getString(R.string.privacy_policy_text),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                text = policy
                movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}