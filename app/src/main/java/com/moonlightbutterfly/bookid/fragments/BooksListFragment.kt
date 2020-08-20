package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.adapters.BookAdapterVertical
import com.moonlightbutterfly.bookid.databinding.BooksListFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.BooksListViewModel

class BooksListFragment: BaseFragment<BooksListFragmentBinding, BooksListViewModel>(BooksListViewModel::class.java)  {

    private val args: BooksListFragmentArgs by navArgs()

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BooksListFragmentBinding.inflate(inflater, container, false).apply {
            this.viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
            this.adapter.apply {
                adapter = BookAdapterVertical()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    override fun initializeCustom(savedInstanceState: Bundle?) {
        viewModel.setCustomShelfId(args.shelfId)
    }
}