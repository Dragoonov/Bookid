package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.adapters.EditShelfsShelfAdapter
import com.moonlightbutterfly.bookid.databinding.EditShelfsFragmentBinding
import com.moonlightbutterfly.bookid.dialogs.RenameShelfDialog
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel

class EditShelfsFragment : BaseFragment<EditShelfsFragmentBinding,ShelfViewModel>(ShelfViewModel::class.java) {

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = EditShelfsFragmentBinding.inflate(inflater, container, false). also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.shelfsRecycler.apply {
                adapter = EditShelfsShelfAdapter(this@EditShelfsFragment)
                layoutManager = LinearLayoutManager(context)
            }
        }
    }
    override fun initializeCustom(savedInstanceState: Bundle?) = (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.myToolbar)

    fun onShelfEditClick(shelf: Shelf) = RenameShelfDialog.newInstance(shelf)
        .show(activity?.supportFragmentManager!!, "RenameShelfDialog")

    fun onShelfDeleteClick(shelf: Shelf) = viewModel.deleteShelf(shelf)

    fun onBookDeleteClick(book: Book, shelf: Shelf) = viewModel.deleteBookFromShelf(book, shelf)

}