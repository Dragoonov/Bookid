package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.adapters.EditShelfsShelfAdapter
import com.moonlightbutterfly.bookid.adapters.ViewPager2Adapter
import com.moonlightbutterfly.bookid.databinding.EditShelfsFragmentBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class EditShelfsFragment: Fragment() {

    private lateinit var binding: EditShelfsFragmentBinding

    companion object {
        fun newInstance(): EditShelfsFragment =
            EditShelfsFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ShelfViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        binding = EditShelfsFragmentBinding.inflate(
            inflater,
            container,
            false)

        viewModel = ViewModelProvider(this,viewModelFactory)[ShelfViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.shelfsRecycler.let {
            it.adapter = EditShelfsShelfAdapter(this)
            it.layoutManager = LinearLayoutManager(context)
        }
        binding.createShelf.setOnClickListener { onShelfCreate("Tescik") }
        viewModel.shelfsLiveData.observe(viewLifecycleOwner, Observer {
            (binding.shelfsRecycler.adapter as EditShelfsShelfAdapter).updateList(it)
        })
        return binding.root
    }

    fun onShelfCreate(name: String) = viewModel.insertShelf(name)

    fun onShelfEditClick(shelf: Shelf) = viewModel.updateShelfName(shelf, "Test")


    fun onShelfDeleteClick(shelf: Shelf) = viewModel.deleteShelf(shelf)


    fun onBookDeleteClick(book: Book, shelf: Shelf) = viewModel.deleteBookFromShelf(book, shelf)

}