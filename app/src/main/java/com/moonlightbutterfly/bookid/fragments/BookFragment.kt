package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.databinding.BookFragmentBinding
import com.moonlightbutterfly.bookid.Converters
import com.moonlightbutterfly.bookid.dialogs.AddBookToShelfDialog
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.viewmodels.BookViewModel
import javax.inject.Inject


class BookFragment : Fragment(){

    private lateinit var binding: BookFragmentBinding

    companion object {
        fun newInstance(book: Book): BookFragment =
             BookFragment().apply {
                 arguments = Bundle().apply { putString("book", Converters.convertToJSONString(book)) }
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
        binding = BookFragmentBinding.inflate(
            inflater,
            container,
            false)

        viewModel = ViewModelProvider(this,viewModelFactory)[BookViewModel::class.java]
        viewModel.setBook(Converters.convertToObject(arguments?.getString("book")) as Book)
        binding.apply {
            viewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            similarBooksInclude.listRecycler.apply {
                layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapter()
            }
            authorBooksInclude.listRecycler.apply {
                layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapter()
            }
//            addToShelf.setOnClickListener { viewModel?.bookLiveData?.value
//                ?.let { AddBookToShelfDialog.newInstance(it) } }
        }
        return binding.root
    }

}
