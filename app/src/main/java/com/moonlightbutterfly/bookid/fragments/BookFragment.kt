package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.Utils
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.databinding.BookFragmentBinding
import com.moonlightbutterfly.bookid.viewmodels.BookViewModel
import javax.inject.Inject


class BookFragment : Fragment(){

    private lateinit var binding: BookFragmentBinding

    companion object {
        fun newInstance(book: String): BookFragment =
             BookFragment().apply {
                 arguments = Bundle().apply { putString("book", book) }
             }

    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: BookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.book_fragment,
            container,
            false)

        viewModel = ViewModelProvider(this,viewModelFactory)[BookViewModel::class.java]
        viewModel.setBook(Utils.convertToObject(arguments?.getString("book")))
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

        viewModel.authorInfoLiveData.observe(viewLifecycleOwner, Observer{
            viewModel.updateDataLoaded()
        })

        viewModel.similarBooksLiveData.observe(viewLifecycleOwner, Observer{
            (binding.similarBooks.adapter as BookAdapter).updateList(it)
            viewModel.updateDataLoaded()
        })

        viewModel.authorBooksLiveData.observe(viewLifecycleOwner, Observer {
            val list = viewModel.removeDisplayedBookFromList()
            (binding.authorBooks.adapter as BookAdapter).updateList(list)
            viewModel.updateDataLoaded()
        })
        viewModel.updateDataLoaded()
        return binding.root
    }

}
