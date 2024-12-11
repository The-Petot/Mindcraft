package com.thepetot.mindcraft.ui.settings

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.databinding.ActivitySettingsProfileBinding
import com.thepetot.mindcraft.ui.ViewModelFactory

class SettingsProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsProfileBinding
    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            if (validateImageResolution(uri)) {
                val intent = Intent(this, CropActivity::class.java)
                intent.putExtra("uri", uri)
                startActivity(intent)
            }
        }
    }

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

        binding.btnProfilePicture.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.password.setOnClickListener {
            settingsBottomSheetPassword.show(
                supportFragmentManager,
                SettingsBottomSheetPassword.TAG
            )
        }

        viewModel.getUser().observe(this) {user ->
            Glide
                .with(binding.root.context)
                .load(user.profilePicture)
                .placeholder(R.drawable.img_profile)
                .error(R.drawable.img_profile)
                .into(binding.imgProfile)
            binding.tvName.text = String.format("${user.firstName} ${user.lastName}")
            binding.tvEmail.text = user.email
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun validateImageResolution(uri: Uri): Boolean {
        val contentResolver = applicationContext.contentResolver
        try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // For API level 28 and above
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                // For API level below 28
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            }

            if (bitmap != null) {
                val width = bitmap.width
                val height = bitmap.height

                if (width < 128 && height < 128) {
                    Toast.makeText(this, "Image resolution too low: $width x $height", Toast.LENGTH_SHORT).show()
                    return false
                }
                return true
            } else {
                Toast.makeText(this, "Unable to load image", Toast.LENGTH_SHORT).show()
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error reading image: ${e.message}", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}
