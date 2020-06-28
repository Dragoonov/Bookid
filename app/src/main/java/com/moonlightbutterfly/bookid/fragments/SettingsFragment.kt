package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.databinding.SettingsFragmentBinding
import javax.inject.Inject


class SettingsFragment : Fragment() {

    @Inject
    lateinit var userManager: UserManager

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        val binding = SettingsFragmentBinding.inflate(inflater,container, false)
        binding.singOut.setOnClickListener {
            userManager.deleteUserFromDatabase(userManager.loggedUser!!)
            userManager.loggedUser = null
            //TODO Navigate to LoginScreen
        }
        binding.textLink.apply {
            val policy = HtmlCompat.fromHtml(getString(R.string.privacy_policy_text), HtmlCompat.FROM_HTML_MODE_LEGACY);
            text = policy
            movementMethod = LinkMovementMethod.getInstance()
        }
        return binding.root
    }
}
