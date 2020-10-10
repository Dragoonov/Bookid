package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.R
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

    override fun initializeViewModel() {
        super.initializeViewModel()
        val favoriteName = requireContext().resources.getStringArray(R.array.basic_shelfs)[0]
        val bookAddedToFavourites = requireContext().getString(R.string.book_added, favoriteName)
        val bookRemovedFromFavourites = requireContext().getString(R.string.book_removed, favoriteName)
        val savedName = requireContext().resources.getStringArray(R.array.basic_shelfs)[1]
        val bookAddedToSaved = requireContext().getString(R.string.book_added, savedName)
        val bookRemovedFromSaved = requireContext().getString(R.string.book_removed, savedName)
        val defaultShelfString = requireContext().getString(R.string.default_shelf)
        val bookAddedToDefaults = requireContext().getString(R.string.book_added, defaultShelfString)
        val error = requireContext().getString(R.string.error_occurred)
        viewModel.apply {
            bookAddedToDefaultsMessage = bookAddedToDefaults
            bookAddedToFavouritesMessage = bookAddedToFavourites
            bookRemovedFromFavouritesMessage = bookRemovedFromFavourites
            bookAddedToSavedMessage = bookAddedToSaved
            bookRemovedFromSavedMessage = bookRemovedFromSaved
            errorOccurredMessage = error
        }
    }

    override fun initializeCustom(savedInstanceState: Bundle?) {
        viewModel.setCustomShelfId(args.shelfId)
    }
}