package com.moonlightbutterfly.bookid.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.moonlightbutterfly.bookid.R

class QuitAppDialog private constructor(): DialogFragment() {

    companion object {
        fun newInstance(): QuitAppDialog = QuitAppDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.quit_question)
                .setPositiveButton(R.string.yes) { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                    activity?.finish()
                }
                .setNegativeButton(R.string.cancel
                ) { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}