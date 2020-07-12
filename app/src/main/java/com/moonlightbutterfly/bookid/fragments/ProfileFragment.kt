package com.moonlightbutterfly.bookid.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.databinding.ProfileFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class ProfileFragment : BaseFragment<ProfileFragmentBinding, ShelfViewModel>(ShelfViewModel::class.java) {

    @Inject
    lateinit var userManager: UserManager

    override fun inject() = (activity?.application as BookidApplication).appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = ProfileFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.userManager = userManager
            it.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun initializeCustom() = (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.myToolbar)
}