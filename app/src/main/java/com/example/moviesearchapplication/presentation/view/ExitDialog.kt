package com.example.moviesearchapplication.presentation.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.moviesearchapplication.R

class ExitDialog() : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(R.string.exit_dialog_msg)
            .setPositiveButton(R.string.Yes){ _, _ ->  (requireContext() as MainActivity).finish()}
            .setNegativeButton(R.string.Cancel){dialog, _ ->  dialog.cancel()}
            .create()
    }
}