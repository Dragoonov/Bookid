package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.moonlightbutterfly.bookid.*
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
            userManager.deleteUserFromDatabase(userManager.loggedUser.value!!)
            findNavController().navigate(R.id.login_graph)
        }
        binding.textLink.apply {
            val policy = HtmlCompat.fromHtml(getString(R.string.privacy_policy_text), HtmlCompat.FROM_HTML_MODE_LEGACY);
            text = policy
            movementMethod = LinkMovementMethod.getInstance()
        }
        (activity as ToolbarManager).showDefaultToolbar()
        return binding.root
    }
}
