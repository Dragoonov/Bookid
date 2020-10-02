package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.SwipeToDeleteCallback
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.BookAdapterVertical
import com.moonlightbutterfly.bookid.databinding.BooksListFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.BooksListViewModel

class BooksListFragment : BaseFragment<BooksListFragmentBinding, BooksListViewModel>(BooksListViewModel::class.java) {

    private val args: BooksListFragmentArgs by navArgs()

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteBookFromCustom((binding?.recycler?.adapter as BookAdapter).books[viewHolder.adapterPosition])
                (binding?.recycler?.adapter as BookAdapter).removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        binding = BooksListFragmentBinding.inflate(inflater, container, false).apply {
            this.viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
            this.recycler.apply {
                adapter = BookAdapterVertical()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                itemTouchHelper.attachToRecyclerView(this)
            }
        }
    }

    override fun initializeCustom(savedInstanceState: Bundle?) {
        viewModel.setCustomShelfId(args.shelfId)
    }
}