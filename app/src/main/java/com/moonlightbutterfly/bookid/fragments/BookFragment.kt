package com.moonlightbutterfly.bookid.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.databinding.BookFragmentBinding
import com.moonlightbutterfly.bookid.Converters
import com.moonlightbutterfly.bookid.CustomItemDecoration
import com.moonlightbutterfly.bookid.adapters.LAYOUT
import com.moonlightbutterfly.bookid.dialogs.AddBookToShelfDialog
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.viewmodels.BookViewModel

class BookFragment : BaseFragment<BookFragmentBinding, BookViewModel>(BookViewModel::class.java){

    override fun inject() = (activity?.application as BookidApplication).appComponent.inject(this)

    override fun initializeViewModel() {
        super.initializeViewModel()
        viewModel.let {
            val bookString = BookFragmentArgs.fromBundle(requireArguments()).book
            val book = Converters.convertToObject(bookString) as Book
            it.setBook(book)
        }
    }

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BookFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.authorBooksInclude.listRecycler.apply {
                addItemDecoration(CustomItemDecoration(LAYOUT.HORIZONTAL))
                layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapter()
            }
            it.addToShelf.setOnClickListener {
                AddBookToShelfDialog
                    .newInstance(viewModel.bookLiveData.value!!)
                    .show(activity?.supportFragmentManager!!, AddBookToShelfDialog.NAME)
            }
        }
    }

    override fun initializeCustom() = (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.myToolbar)


}
