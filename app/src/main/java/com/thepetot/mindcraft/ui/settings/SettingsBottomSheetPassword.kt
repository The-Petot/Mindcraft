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

class SettingsBottomSheetPassword : BottomSheetDialogFragment() {

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {
        val view = inflater.inflate(R.layout.bottom_sheet_password, container, false)

        val userId = viewModel.getPreferenceSettings(UserPreference.USERID_KEY, 0)
        val password = view.findViewById<TextInputEditText>(R.id.et_password)
        val confirmPassword = view.findViewById<TextInputEditText>(R.id.et_confirm_password)
        val passwordLayout = view.findViewById<TextInputLayout>(R.id.et_layout_password)
        val confirmPasswordLayout = view.findViewById<TextInputLayout>(R.id.et_layout_confirm_password)
        val progressBar = view.findViewById<View>(R.id.progress_bar)

        password.doOnTextChanged { text, _, _, _ ->
            if (text != null && text.length < 8) {
                passwordLayout.error = "Minimum 8 characters"
            } else {
                passwordLayout.error = null
            }
        }

        confirmPassword.doOnTextChanged { text, _, _, _ ->
            if (text != null && text.toString() != password.text.toString()) {
                confirmPasswordLayout.error = "Password doesn't match"
            } else {
                confirmPasswordLayout.error = null
            }
        }

        view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            // Close the bottom sheet
            dismiss()
        }

        view.findViewById<Button>(R.id.btn_save).setOnClickListener {
            when {
                passwordLayout.error != null || password.text.isNullOrEmpty() -> {
                    password.requestFocus()
                    passwordLayout.error = passwordLayout.error ?: "Password is required"
                }
                confirmPasswordLayout.error != null || confirmPassword.text.isNullOrEmpty() -> {
                    confirmPassword.requestFocus()
                    confirmPasswordLayout.error = confirmPasswordLayout.error ?: "Confirm Password is required"
                }
                else -> {
                    viewModel.updateUser(
                        userId = userId,
                        password = password.text.toString()
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
                                dismiss()
                            }
                        }
                    }
                }
            }
        }



        return view
    }

    companion object {
        const val TAG = "BottomSheetPassword"
    }
}