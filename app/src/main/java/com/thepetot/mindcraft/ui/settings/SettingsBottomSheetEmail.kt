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

class SettingsBottomSheetEmail : BottomSheetDialogFragment() {

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_email, container, false)
        val userId = viewModel.getPreferenceSettings(UserPreference.USERID_KEY, 0)
        val email = view.findViewById<TextInputEditText>(R.id.et_email)
        val emailLayout = view.findViewById<TextInputLayout>(R.id.et_layout_email)
        val progressBar = view.findViewById<View>(R.id.progress_bar)


        view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            // Close the bottom sheet
            dismiss()
        }

        email.doOnTextChanged { text, _, _, _ ->
            if (text != null && !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                emailLayout.error = "Invalid email"
            } else {
                emailLayout.error = null
            }
        }

        view.findViewById<Button>(R.id.btn_save).setOnClickListener {
            when {
                emailLayout.error != null || email.text.isNullOrEmpty() -> {
                    email.requestFocus()
                    emailLayout.error = emailLayout.error ?: "Email is required"
                }
                else -> {
                    viewModel.updateUser(
                        userId = userId,
                        email = email.text.toString()
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
                                    viewModel.setPreferenceSettingsSuspend(UserPreference.EMAIL_KEY, email.text.toString())
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
        const val TAG = "BottomSheetEmail"
    }
}