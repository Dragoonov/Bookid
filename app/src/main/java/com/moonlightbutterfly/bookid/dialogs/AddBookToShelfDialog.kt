package com.moonlightbutterfly.bookid.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.Communicator
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.AddBookToShelfDialogBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class AddBookToShelfDialog private constructor(private val book: Book): DialogFragment() {
    companion object {
        fun newInstance(book: Book): AddBookToShelfDialog = AddBookToShelfDialog(book)
        const val NAME = "AddBookToShelfDialog"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var communicator: Communicator

    private lateinit var viewModel: ShelfViewModel

    private lateinit var binding: AddBookToShelfDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (activity?.application as BookidApplication).appComponent.inject(this)
        return activity?.let { fragment ->
            val builder = AlertDialog.Builder(fragment)
            initializeDependencies()
            builder
                .setView(binding.root)
                .setPositiveButton(R.string.ok) { _, _ ->
                    val idx: Int = binding.radioGroup.returnSelectedIndex()
                    if (idx >= 0) {
                        viewModel.shelfsLiveData.value?.get(idx)?.let {
                                viewModel.insertBookToShelf(it, book)
                            communicator.postMessage(getString(R.string.book_added))
                            }
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ -> dialog?.cancel() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initializeDependencies() {
        viewModel = ViewModelProvider(this,viewModelFactory)[ShelfViewModel::class.java]
        binding = AddBookToShelfDialogBinding.inflate(layoutInflater).also {
            it.viewModel = viewModel
            it.lifecycleOwner = this
        }
    }

    private fun RadioGroup.returnSelectedIndex(): Int = indexOfChild(findViewById<RadioButton>(checkedRadioButtonId))
}