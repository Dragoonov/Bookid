package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.SettingsFragmentBinding
import javax.inject.Inject


class SettingsFragment : BaseFragment<SettingsFragmentBinding, ViewModel>() {

    @Inject
    lateinit var userManager: Manager

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SettingsFragmentBinding.inflate(inflater,container, false).also {
            it.userManager = userManager
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
