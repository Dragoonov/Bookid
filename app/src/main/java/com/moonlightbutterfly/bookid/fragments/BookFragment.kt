package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.moonlightbutterfly.bookid.Converters
import com.moonlightbutterfly.bookid.CustomItemDecoration
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.databinding.BookFragmentBinding
import com.moonlightbutterfly.bookid.dialogs.AddBookToShelfDialog
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.viewmodels.BookViewModel
import kotlin.math.abs

class BookFragment : BaseFragment<BookFragmentBinding, BookViewModel>(BookViewModel::class.java) {

    private val args: BookFragmentArgs by navArgs()

    private val offsetChangedListener = AppBarLayout.OnOffsetChangedListener {layout: AppBarLayout, i: Int ->
        if (abs(i) >= layout.totalScrollRange) {
            binding?.toolbar?.appTitle?.visibility = View.VISIBLE
        } else {
            binding?.toolbar?.appTitle?.visibility = View.INVISIBLE
        }
    }


    override fun inject() = appComponent.inject(this)

    override fun initializeViewModel() {
        super.initializeViewModel()
        viewModel.let {
            val bookString = args.book
            val book = Converters.convertToObject(bookString) as Book?
            it.setBook(book!!)
        }
    }

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BookFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.authorBooks.listRecycler.apply {
                addItemDecoration(CustomItemDecoration(LAYOUT.HORIZONTAL))
                layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapter()
            }
            it.similarBooks.listRecycler.apply {
                addItemDecoration(CustomItemDecoration(LAYOUT.HORIZONTAL))
                layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapter()
            }
            it.addToShelf.setOnClickListener {
                AddBookToShelfDialog
                    .newInstance(viewModel.bookLiveData.value!!)
                    .show(activity?.supportFragmentManager!!, AddBookToShelfDialog.NAME)
            }
            it.appBar.addOnOffsetChangedListener(offsetChangedListener)
        }
    }

    override fun initializeCustom(savedInstanceState: Bundle?) =
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.myToolbar)
}
