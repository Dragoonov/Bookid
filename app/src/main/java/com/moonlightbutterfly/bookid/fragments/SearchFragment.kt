package com.moonlightbutterfly.bookid.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.CustomItemDecoration
import com.moonlightbutterfly.bookid.DrawerManager
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.databinding.SearchFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.SearchViewModel
import javax.inject.Inject


class SearchFragment : BaseFragment<SearchFragmentBinding, SearchViewModel>(SearchViewModel::class.java) {

    @Inject
    lateinit var userManager: Manager

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
            if(dy != 0) {
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
        val itemDecor = CustomItemDecoration(LAYOUT.VERTICAL)
        binding = SearchFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.recyclerLayout.listRecycler.apply {
                adapter = BookAdapter(LAYOUT.VERTICAL)
                addItemDecoration(itemDecor)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addOnScrollListener(onScrollListener)
                setHasFixedSize(true)
            }
            it.toolbar.searchView.setOnQueryTextListener(onQueryChangeListener)
        }
    }

    override fun initializeCustom() {
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.searchToolbar)
        (activity as DrawerManager).unlockDrawer()
    }
}