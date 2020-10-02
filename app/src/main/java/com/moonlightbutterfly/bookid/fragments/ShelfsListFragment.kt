package com.moonlightbutterfly.bookid.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.adapters.ShelfAdapter
import com.moonlightbutterfly.bookid.databinding.ShelfsListFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel

class ShelfsListFragment : BaseFragment<ShelfsListFragmentBinding, ShelfViewModel>(ShelfViewModel::class.java) {

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = ShelfsListFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.shelfsAdapter.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = ShelfAdapter(viewModel)
            }
            it.floatingActionButton.setOnClickListener { view ->
                val action = ShelfsListFragmentDirections
                    .actionShelfsToCreateEditShelfFragment(view.context.getString(R.string.create_shelf))
                view.findNavController().navigate(action)
            }
        }
    }

}