package com.thepetot.mindcraft.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thepetot.mindcraft.R

class SettingsBottomSheetPassword : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_password, container, false)

    companion object {
        const val TAG = "BottomSheetPassword"
    }
}