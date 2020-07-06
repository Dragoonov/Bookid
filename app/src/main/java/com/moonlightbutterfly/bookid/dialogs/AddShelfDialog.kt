package com.moonlightbutterfly.bookid.dialogs

import android.app.Dialog
import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.ShelfDialogBinding
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject

class AddShelfDialog private constructor(): DialogFragment() {

    companion object {
        fun newInstance(): AddShelfDialog = AddShelfDialog()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ShelfViewModel


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (activity?.application as BookidApplication).appComponent.inject(this)
        val layout = requireActivity().layoutInflater.inflate(R.layout.shelf_dialog, null)
        layout.findViewById<TextView>(R.id.title_text).text = getString(R.string.create_shelf)
        viewModel = ViewModelProvider(this,viewModelFactory)[ShelfViewModel::class.java]
            val builder = AlertDialog.Builder(requireActivity())
                .setView(layout)
                .setPositiveButton(R.string.ok) { _, _ ->
                    val name = layout.findViewById<EditText>(R.id.shelf_name).text.toString()
                    if (name.isNotEmpty()) {
                        viewModel.insertShelf(name)
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ -> dialog?.cancel() }
            return builder.create()
    }
}