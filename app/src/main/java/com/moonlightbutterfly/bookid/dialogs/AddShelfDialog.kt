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
        const val NAME = "AddShelfDialog"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (activity?.application as BookidApplication).appComponent.inject(this)
        val viewModel = ViewModelProvider(this,viewModelFactory)[ShelfViewModel::class.java]
        val binding = ShelfDialogBinding.inflate(layoutInflater)

            val builder = AlertDialog.Builder(requireActivity())
                .setView(binding.root)
                .setPositiveButton(R.string.ok) { _, _ ->
                    val name = binding.shelfName.text.toString()
                    if (name.isNotEmpty()) {
                        viewModel.insertShelf(name)
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ -> dialog?.cancel() }
            return builder.create()
    }
}