package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.adapters.BookAdapter
import com.moonlightbutterfly.bookid.databinding.BookFragmentBinding
import com.moonlightbutterfly.bookid.Converters
import com.moonlightbutterfly.bookid.ToolbarManager
import com.moonlightbutterfly.bookid.dialogs.AddBookToShelfDialog
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.viewmodels.BookViewModel
import javax.inject.Inject


class BookFragment : Fragment(){

    private var binding: BookFragmentBinding? = null
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
        viewModel.setBook(Converters.convertToObject(BookFragmentArgs.fromBundle(requireArguments()).book) as Book)
        binding?.let {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.similarBooksInclude.listRecycler.apply {
                layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapter()
            }
            it.authorBooksInclude.listRecycler.apply {
                layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = BookAdapter()
            }
            it.addToShelf.setOnClickListener { viewModel.bookLiveData.value
                ?.let { book -> AddBookToShelfDialog.newInstance(book)
                    .show(activity?.supportFragmentManager!!, "AddBookToShelfDialog") } }
        }
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.myToolbar)
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
