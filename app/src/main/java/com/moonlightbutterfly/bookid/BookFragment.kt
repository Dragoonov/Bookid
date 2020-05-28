package com.moonlightbutterfly.bookid

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.moonlightbutterfly.bookid.databinding.BookFragmentBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.viewmodels.BookViewModel


class BookFragment : Fragment() {

    companion object {
        fun newInstance(book: Book, author: Author) = BookFragment().apply {
            viewModel.book.value = book
            viewModel.author.value = author
        }
    }

    private lateinit var viewModel: BookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: BookFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.book_fragment,
            container,
            false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BookViewModel::class.java)
    }

}
