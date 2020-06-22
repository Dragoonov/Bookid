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
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.databinding.SearchFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.SearchViewModel
import javax.inject.Inject


class SearchFragment : Fragment() {

    private lateinit var binding: SearchFragmentBinding

    companion object {
        fun newInstance() = SearchFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        binding = SearchFragmentBinding.inflate(
            inflater,
            container,
            false)

        viewModel = ViewModelProvider(this,viewModelFactory)[SearchViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerLayout.listRecycler.let {
            it.adapter = BookAdapter(LAYOUT.VERTICAL)
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = (it.layoutManager as LinearLayoutManager).itemCount
                    val lastVisibleItem = (it.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (lastVisibleItem+1 >= totalItemCount && viewModel.allDataLoaded.value!!) {
                        viewModel.loadMore()
                    }
                }
            })
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = true


            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty() || newText == viewModel.currentQuery) {
                    return false
                }
                viewModel.clearData()
                (binding.recyclerLayout.listRecycler.adapter as BookAdapter).clearList()
                viewModel.requestSearch(newText)
                return false
            }

        })
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }


}