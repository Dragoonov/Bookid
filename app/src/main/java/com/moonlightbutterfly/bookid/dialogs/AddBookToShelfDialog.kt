package com.moonlightbutterfly.bookid.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.R
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

            val layout = requireActivity().layoutInflater.inflate(R.layout.add_book_to_shelf_dialog, null)
//            val binding = AddBookToShelfDialogBinding.inflate(layoutInflater).apply {
//                lifecycleOwner = viewLifecycleOwner
//            }
            viewModel = ViewModelProvider(this,viewModelFactory)[ShelfViewModel::class.java]
            val radioGroup = layout.findViewById<RadioGroup>(R.id.radio_group)
            viewModel.shelfsLiveData.observe(this, Observer {
                it.forEach {
                    radioGroup.addView(RadioButton(context).apply {
                        text = it.name
                    })
                }
            })
            builder.setView(layout)
                .setPositiveButton(R.string.ok) { _, _ ->
                    if(radioGroup.checkedRadioButtonId >= 0) {
                        val radioButtonID: Int = radioGroup.checkedRadioButtonId
                        val radioButton: View = radioGroup.findViewById(radioButtonID)
                        val idx: Int = radioGroup.indexOfChild(radioButton)
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