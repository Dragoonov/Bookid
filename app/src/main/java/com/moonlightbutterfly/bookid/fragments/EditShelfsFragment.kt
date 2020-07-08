package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.adapters.EditShelfsShelfAdapter
import com.moonlightbutterfly.bookid.databinding.EditShelfsFragmentBinding
import com.moonlightbutterfly.bookid.dialogs.RenameShelfDialog
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class EditShelfsFragment : Fragment() {

    private var binding: EditShelfsFragmentBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ShelfViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this,viewModelFactory)[ShelfViewModel::class.java]
        binding = EditShelfsFragmentBinding.inflate(inflater, container, false). also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.shelfsRecycler.apply {
                adapter = EditShelfsShelfAdapter(this@EditShelfsFragment)
                layoutManager = LinearLayoutManager(context)
            }
        }
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar?.myToolbar)
        return binding?.root
    }

    fun onShelfEditClick(shelf: Shelf) = RenameShelfDialog.newInstance(shelf)
        .show(activity?.supportFragmentManager!!, "RenameShelfDialog")


    fun onShelfDeleteClick(shelf: Shelf) = viewModel.deleteShelf(shelf)


    fun onBookDeleteClick(book: Book, shelf: Shelf) = viewModel.deleteBookFromShelf(book, shelf)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}