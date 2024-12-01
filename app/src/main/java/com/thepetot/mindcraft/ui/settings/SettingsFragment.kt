package com.thepetot.mindcraft.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.thepetot.mindcraft.R as appR
import com.thepetot.mindcraft.databinding.FragmentSettingsBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.ui.login.LoginActivity
import com.thepetot.mindcraft.utils.SharedPreferencesManager

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        viewModel.getTheme(requireActivity().applicationContext)
        viewModel.currentThemeSetting.observe(viewLifecycleOwner) {
            binding.tvCurrentTheme.text = it
            when (it) {
                SYSTEM_DEFAULT_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                LIGHT_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                DARK_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
        setupButton()
    }

    private fun setupButton() {
        val switch2fa = binding.switch2fa
        val switchNotification = binding.switchNotification

        switch2fa.isChecked = viewModel.is2FASwitchChecked
        switch2fa.jumpDrawablesToCurrentState()

        switchNotification.isChecked = viewModel.isNotificationSwitchChecked
        switchNotification.jumpDrawablesToCurrentState()

        switch2fa.setOnCheckedChangeListener { _, isChecked ->
            viewModel.is2FASwitchChecked = isChecked
        }

        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isNotificationSwitchChecked = isChecked
        }

        binding.profile.setOnClickListener { navigateToProfilePage() }

        binding.twoStepVerification.setOnClickListener {
            switch2fa.isChecked = !viewModel.is2FASwitchChecked
        }

        binding.notification.setOnClickListener {
            switchNotification.isChecked = !viewModel.isNotificationSwitchChecked
        }

        binding.btnLogout.setOnClickListener {
            SharedPreferencesManager.set(requireActivity().applicationContext, USER_LOGGED_IN, false)
            viewModel.deleteDataUser()
            navigateToLoginPage()
        }

        binding.theme.setOnClickListener { showMenu() }
    }

    private fun showMenu() {
        val popup = PopupMenu(context, binding.menuTheme)
        popup.menuInflater.inflate(appR.menu.popup_menu, popup.menu)

        popup.setOnMenuItemClickListener {
            // TODO: Fix all icon color on dark mode
            when (it.itemId) {
                appR.id.system_default -> {
                    SharedPreferencesManager.set(requireActivity().applicationContext, APP_THEME, SYSTEM_DEFAULT_THEME)
                    viewModel.getTheme(requireActivity().applicationContext)
                    true
                }
                appR.id.light -> {
                    SharedPreferencesManager.set(requireActivity().applicationContext, APP_THEME, LIGHT_THEME)
                    viewModel.getTheme(requireActivity().applicationContext)
                    true
                }
                appR.id.dark -> {
                    SharedPreferencesManager.set(requireActivity().applicationContext, APP_THEME, DARK_THEME)
                    viewModel.getTheme(requireActivity().applicationContext)
                    true
                }
                else -> false
            }
        }

        // Respond to popup being dismissed.
        // popup.setOnDismissListener { }
        popup.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToLoginPage() {
        val loginIntent = Intent(context, LoginActivity::class.java)
        startActivity(loginIntent)
        requireActivity().finish()
    }

    private fun navigateToProfilePage() {
        val intent = Intent(context, SettingsProfileActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val USER_LOGGED_IN = "user_logged_in"

        const val APP_THEME = "app_theme"
        const val DARK_THEME = "Dark"
        const val LIGHT_THEME = "Light"
        const val SYSTEM_DEFAULT_THEME = "System Default"
    }
}
