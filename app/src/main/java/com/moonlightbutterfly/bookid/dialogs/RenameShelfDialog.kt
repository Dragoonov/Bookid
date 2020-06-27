package com.moonlightbutterfly.bookid.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.ShelfDialogBinding
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class RenameShelfDialog(private val shelf: Shelf) : DialogFragment() {

    companion object {
        fun newInstance(shelf: Shelf): RenameShelfDialog = RenameShelfDialog(shelf)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ShelfViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (activity?.application as BookidApplication).appComponent.inject(this)
        return activity?.let {
            val builder = AlertDialog.Builder(it)

//            val binding = ShelfDialogBinding.inflate(layoutInflater).apply {
//                lifecycleOwner = viewLifecycleOwner
//                titleText.text = getString(R.string.rename_shelf)
//            }
            val layout = requireActivity().layoutInflater.inflate(R.layout.shelf_dialog, null)
            layout.findViewById<TextView>(R.id.title_text).text = getString(R.string.rename_shelf)
            viewModel = ViewModelProvider(this,viewModelFactory)[ShelfViewModel::class.java]
            builder.setView(layout)
                .setPositiveButton(R.string.ok) { _, _ ->
                    val name = layout.findViewById<EditText>(R.id.shelf_name).text.toString()
                    if (name.isNotEmpty()) {
                        viewModel.updateShelfName(shelf, name)
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ -> dialog?.cancel() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}