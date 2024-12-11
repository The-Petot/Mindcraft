package com.thepetot.mindcraft.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.utils.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsBottomSheetName : BottomSheetDialogFragment() {

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_name, container, false)

        val userId = viewModel.getPreferenceSettings(UserPreference.USERID_KEY, 0)
        val firstName = view.findViewById<TextInputEditText>(R.id.et_first_name)
        val firstNameLayout = view.findViewById<TextInputLayout>(R.id.et_layout_first_name)
        val lastName = view.findViewById<TextInputEditText>(R.id.et_last_name)
        val lastNameLayout = view.findViewById<TextInputLayout>(R.id.et_layout_last_name)
        val progressBar = view.findViewById<View>(R.id.progress_bar)
        
        view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            // Close the bottom sheet
            dismiss()
        }

        firstName.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (!it.matches("^[a-zA-Z]*$".toRegex())) {
                    firstNameLayout.error = "Only letters"
                } else {
                    firstNameLayout.error = null
                }
            }
        }

        lastName.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (!it.matches("^[a-zA-Z]*$".toRegex())) {
                    lastNameLayout.error = "Only letters"
                } else {
                    lastNameLayout.error = null
                }
            }
        }

        view.findViewById<Button>(R.id.btn_save).setOnClickListener {
            when {
                firstNameLayout.error != null || firstName.text.isNullOrEmpty() -> {
                    firstName.requestFocus()
                    firstNameLayout.error = firstNameLayout.error ?: "First Name is required"
                }
                lastNameLayout.error != null || lastName.text.isNullOrEmpty() -> {
                    lastName.requestFocus()
                    lastNameLayout.error = lastNameLayout.error ?: "Last Name is required"
                }
                else -> {
                    viewModel.updateUser(
                        userId = userId,
                        firstName = firstName.text.toString(),
                        lastName = lastName.text.toString()
                    ).observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Error -> {
                                progressBar.visibility = View.INVISIBLE
                                Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                            }
                            is Result.Loading -> {
                                progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                progressBar.visibility = View.INVISIBLE
                                Toast.makeText(context, result.data.message, Toast.LENGTH_SHORT).show()
                                lifecycleScope.launch {
                                    viewModel.setPreferenceSettingsSuspend(UserPreference.FIRST_NAME_KEY, firstName.text.toString())
                                    viewModel.setPreferenceSettingsSuspend(UserPreference.LAST_NAME_KEY, lastName.text.toString())
                                    dismiss()
                                }
                            }
                        }
                    }
                }
            }
        }

        return view
    }


    companion object {
        const val TAG = "BottomSheetName"
    }
}