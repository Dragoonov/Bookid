package com.moonlightbutterfly.bookid

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.databinding.BookFragmentBinding
import com.moonlightbutterfly.bookid.repository.externalrepos.goodreads.GoodreadsRepository
import com.moonlightbutterfly.bookid.viewmodels.BookViewModel


class BookFragment : Fragment() {


    companion object {
        fun newInstance(book: String): BookFragment =
             BookFragment().apply {
                 arguments = Bundle().apply { putString("book", book) }
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


        viewModel = ViewModelProviders.of(this).get(BookViewModel::class.java)
        viewModel.init(book = Utils.convertToObject(arguments?.getString("book")!!),repository =  GoodreadsRepository())
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.authorBooks.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false)
            adapter = BookAdapter()
        }

        binding.similarBooks.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false)
            adapter = BookAdapter()
        }

        viewModel.authorsBooks.observe(viewLifecycleOwner, Observer{
            (binding.authorBooks.adapter as BookAdapter).updateList(it)
        })

        viewModel.similarBooks.observe(viewLifecycleOwner, Observer{
            (binding.similarBooks.adapter as BookAdapter).updateList(it)
        })
        return binding.root
    }

}
