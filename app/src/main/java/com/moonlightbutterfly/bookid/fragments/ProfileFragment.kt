package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.databinding.ComposableBookListBinding
import com.moonlightbutterfly.bookid.databinding.ProfileFragmentBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.viewmodels.ProfileViewModel
import javax.inject.Inject

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance(): ProfileFragment =
            ProfileFragment()

    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProfileViewModel

    private lateinit var binding: ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val booksList = ArrayList<Book>().apply {
            add(
                Book(
                    1,
                    "title1", Author(1, "author", "imageurl"), "pubdate", 1.2, "image"
                )
            )
            add(
                Book(
                    1,
                    "title1", Author(1, "author", "imageurl"), "pubdate", 1.2, "image"
                )
            )
            add(
                Book(
                    1,
                    "title1", Author(1, "author", "imageurl"), "pubdate", 1.2, "image"
                )
            )
        }
        val shelfListMock = ArrayList<Shelf>().apply {
            add(
                Shelf(
                    1,
                    "Polka1",
                    booksList,
                    1
                )
            )
            add(
                Shelf(
                    1,
                    "Polka2",
                    booksList,
                    1
                )
            )
            add(
                Shelf(
                    1,
                    "Polka3",
                    booksList,
                    1
                )
            )
        }
        (activity?.application as BookidApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        viewModel.shelfsLiveData.observe(viewLifecycleOwner, Observer {
            createBookShelfs(it)
            addFooter()
        })
        binding = ProfileFragmentBinding
            .inflate(
                inflater,
                container, false
            )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        createBookShelfs(shelfListMock)
        addFooter()
        return binding.root
    }

    private fun addFooter() {
        layoutInflater.inflate(R.layout.footer,binding.layoutViewContainer,true)
    }

    private fun createBookShelfs(shelfsList: List<Shelf>) {
        for (shelf in shelfsList) {
            ComposableBookListBinding.inflate(
                layoutInflater,
                binding.layoutViewContainer,
                true).also {
                it.listRecycler.apply {
                    layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = BookAdapter()
                }
                it.books = shelf.books
                it.title = shelf.name
                it.lifecycleOwner = viewLifecycleOwner
            }
        }
    }
}