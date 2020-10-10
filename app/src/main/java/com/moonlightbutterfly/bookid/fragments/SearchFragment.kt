package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.BookAdapterVertical
import com.moonlightbutterfly.bookid.databinding.SearchFragmentBinding
import com.moonlightbutterfly.bookid.getNavController
import com.moonlightbutterfly.bookid.viewmodels.SearchViewModel


class SearchFragment : BaseFragment<SearchFragmentBinding, SearchViewModel>(SearchViewModel::class.java) {

    private val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val manager: LinearLayoutManager = binding?.recyclerLayout?.listRecycler?.layoutManager
                    as LinearLayoutManager
            super.onScrolled(recyclerView, dx, dy)
            val totalItemCount = manager.itemCount
            val lastVisibleItem = manager.findLastVisibleItemPosition()
            if (lastVisibleItem + 1 >= totalItemCount && viewModel.allDataLoaded.value!!) {
                viewModel.loadMore()
            }
            if (dy != 0) {
                binding?.toolbar?.searchView?.clearFocus()
            }
        }
    }

    private val onQueryChangeListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean = true

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText.isNullOrEmpty() || newText == viewModel.currentQuery) {
                return false
            }
            (binding?.recyclerLayout?.listRecycler?.adapter as BookAdapter).clearList()
            viewModel.requestSearch(newText)
            return false
        }
    }

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SearchFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.recyclerLayout.listRecycler.apply {
                adapter = BookAdapterVertical(true, viewModel)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addOnScrollListener(onScrollListener)
            }
            it.toolbar.searchView.setOnQueryTextListener(onQueryChangeListener)
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
        context?.getNavController()?.unlockBottomNav()
        viewModel.favoriteShelfLiveData.observe(viewLifecycleOwner, Observer {  })
        viewModel.savedShelfLiveData.observe(viewLifecycleOwner, Observer {  })
    }

}