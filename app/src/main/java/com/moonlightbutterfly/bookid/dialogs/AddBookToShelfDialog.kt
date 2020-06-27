package com.moonlightbutterfly.bookid.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.RadioButton
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

class AddBookToShelfDialog(private val book: Book): DialogFragment() {
    companion object {
        fun newInstance(book: Book): AddBookToShelfDialog = AddBookToShelfDialog(book)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ShelfViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (activity?.application as BookidApplication).appComponent.inject(this)
        return activity?.let { fragment ->
            val builder = AlertDialog.Builder(fragment)

            val binding = AddBookToShelfDialogBinding.inflate(layoutInflater).apply {
                lifecycleOwner = viewLifecycleOwner
            }
            viewModel = ViewModelProvider(this,viewModelFactory)[ShelfViewModel::class.java]
            viewModel.shelfsLiveData.observe(viewLifecycleOwner, Observer {
                it.forEach {
                    binding.radioGroup.addView(RadioButton(context).apply {
                        text = it.name
                    })
                }
            })
            builder.setView(binding.root)
                .setPositiveButton(R.string.ok) { _, _ ->
                    viewModel.shelfsLiveData.value?.get(binding.radioGroup.checkedRadioButtonId)?.let {
                        viewModel.insertBookToShelf(it,book)
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ -> dialog?.cancel() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}