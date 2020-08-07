package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

    override fun initializeCustom(savedInstanceState: Bundle?) = (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.myToolbar)
}