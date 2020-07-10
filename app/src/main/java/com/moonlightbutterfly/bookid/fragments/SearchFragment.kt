package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.DrawerManager
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.databinding.SearchFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.SearchViewModel
import javax.inject.Inject


class SearchFragment : Fragment() {

    private var binding: SearchFragmentBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userManager: UserManager

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this,viewModelFactory)[SearchViewModel::class.java]
        binding = SearchFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.recyclerLayout.listRecycler.apply {
                adapter = BookAdapter(LAYOUT.VERTICAL)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val totalItemCount = (layoutManager as LinearLayoutManager).itemCount
                        val lastVisibleItem =
                            (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        if (lastVisibleItem + 1 >= totalItemCount && viewModel.allDataLoaded.value!!) {
                            viewModel.loadMore()
                        }
                    }
                })
                setHasFixedSize(true)
            }
            it.toolbar.searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean = true


                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!viewModel.showHint) {
                        it.booksSearchHint.visibility = View.GONE
                    }
                    if (newText.isNullOrEmpty() || newText == viewModel.currentQuery) {
                        return false
                    }
                    it.booksSearchHint.visibility = View.GONE
                    viewModel.clearData()
                    (it.recyclerLayout.listRecycler.adapter as BookAdapter).clearList()
                    viewModel.requestSearch(newText)
                    viewModel.showHint = false
                    return false
                }

            })
        }
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.searchToolbar)
        (activity as DrawerManager).unlockDrawer()
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}