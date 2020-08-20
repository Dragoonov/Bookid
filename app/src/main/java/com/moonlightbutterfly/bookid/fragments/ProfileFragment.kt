package com.moonlightbutterfly.bookid.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.databinding.ProfileFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class ProfileFragment : BaseFragment<ProfileFragmentBinding, ShelfViewModel>(ShelfViewModel::class.java) {

    @Inject
    lateinit var userManager: Manager

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = ProfileFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.userManager = userManager
            it.lifecycleOwner = viewLifecycleOwner
        }
    }
}