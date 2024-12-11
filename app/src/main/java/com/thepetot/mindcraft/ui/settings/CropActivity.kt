package com.thepetot.mindcraft.ui.settings

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.databinding.ActivityCropBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.utils.Result
import com.thepetot.mindcraft.utils.reduceFileImage
import com.thepetot.mindcraft.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CropActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCropBinding
    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCropBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("uri", Uri::class.java)
        } else {
            intent.getParcelableExtra("uri") as? Uri
        }

        val option = CropImageOptions()
        option.fixAspectRatio = true
        binding.cropImageView.setImageCropOptions(option)

        binding.cropImageView.setImageUriAsync(uri)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.cropImageView.setOnCropImageCompleteListener { _, result ->
            result.uriContent?.let {
                lifecycleScope.launch {
                    val imageFile = withContext(Dispatchers.IO) {
                        uriToFile(it, this@CropActivity).reduceFileImage()
                    }
                    val userId = viewModel.getPreferenceSettings(UserPreference.USERID_KEY, 0)

                    viewModel.updateUser(userId, image = imageFile).observe(this@CropActivity) { result ->
                        when (result) {
                            is Result.Error -> {
                                binding.progressBar.visibility = View.INVISIBLE
                                Toast.makeText(this@CropActivity, result.error, Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.INVISIBLE
                                lifecycleScope.launch {
                                    viewModel.setPreferenceSettings(UserPreference.PROFILE_PICTURE_KEY, result.data.data.profileImgUrl)
                                    Toast.makeText(this@CropActivity, result.data.message, Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.toolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.action_crop -> {
                    binding.cropImageView.croppedImageAsync()
                    true
                }

                R.id.action_rotate -> {
                    binding.cropImageView.rotateImage(90)
                    true
                }

                else -> false
            }
        }
    }
}