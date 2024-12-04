package com.thepetot.mindcraft.ui.settings

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.thepetot.mindcraft.databinding.ActivitySettingsProfileBinding

class SettingsProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.right)
            insets
        }

        val settingsBottomSheetName = SettingsBottomSheetName()
        val settingsBottomSheetEmail = SettingsBottomSheetEmail()
        val settingsBottomSheetPassword = SettingsBottomSheetPassword()

        binding.name.setOnClickListener {
            settingsBottomSheetName.show(supportFragmentManager, SettingsBottomSheetName.TAG)
        }

        binding.email.setOnClickListener {
            settingsBottomSheetEmail.show(supportFragmentManager, SettingsBottomSheetEmail.TAG)
        }

        binding.password.setOnClickListener {
            settingsBottomSheetPassword.show(
                supportFragmentManager,
                SettingsBottomSheetPassword.TAG
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
