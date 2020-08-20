package com.moonlightbutterfly.bookid.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.adapters.ShelfAdapter
import com.moonlightbutterfly.bookid.databinding.ShelfFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel

class ShelfFragment : BaseFragment<ShelfFragmentBinding, ShelfViewModel>(ShelfViewModel::class.java) {

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = ShelfFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.shelfsAdapter.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = ShelfAdapter(viewModel)
            }
        }
    }

}