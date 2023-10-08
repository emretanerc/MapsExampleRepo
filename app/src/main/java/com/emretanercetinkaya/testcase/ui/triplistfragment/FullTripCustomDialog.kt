package com.emretanercetinkaya.testcase.ui.triplistfragment


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

import com.emretanercetinkaya.testcase.databinding.TripCustomFullDialogBinding
class FullTripCustomDialog : DialogFragment() {

    private lateinit var binding: TripCustomFullDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TripCustomFullDialogBinding.inflate(layoutInflater)

        binding.selectTripButton.setOnClickListener {
            dialog?.dismiss()
        }

        return binding.root
    }


}

