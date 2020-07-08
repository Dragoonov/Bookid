package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.moonlightbutterfly.bookid.*
import com.moonlightbutterfly.bookid.databinding.SettingsFragmentBinding
import javax.inject.Inject


class SettingsFragment : Fragment() {

    @Inject
    lateinit var userManager: UserManager

    private var binding: SettingsFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
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
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.myToolbar)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
