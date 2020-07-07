package com.moonlightbutterfly.bookid.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.AddBookToShelfDialogBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class AddBookToShelfDialog private constructor(private val book: Book): DialogFragment() {
    companion object {
        fun newInstance(book: Book): AddBookToShelfDialog = AddBookToShelfDialog(book)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ShelfViewModel

    private lateinit var binding: AddBookToShelfDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (activity?.application as BookidApplication).appComponent.inject(this)
        return activity?.let { fragment ->
            val builder = AlertDialog.Builder(fragment)
            viewModel = ViewModelProvider(this,viewModelFactory)[ShelfViewModel::class.java]
            binding = AddBookToShelfDialogBinding.inflate(layoutInflater).also {
                it.viewModel = viewModel
                it.lifecycleOwner = this
            }
            builder.setView(binding.root)
                .setPositiveButton(R.string.ok) { _, _ ->
                    if(binding.radioGroup.checkedRadioButtonId >= 0) {
                        val radioButtonID: Int = binding.radioGroup.checkedRadioButtonId
                        val radioButton: View = binding.radioGroup.findViewById(radioButtonID)
                        val idx: Int = binding.radioGroup.indexOfChild(radioButton)
                        viewModel.shelfsLiveData.value?.get(idx)
                            ?.let {
                                viewModel.insertBookToShelf(it, book)
                            }
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ -> dialog?.cancel() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}