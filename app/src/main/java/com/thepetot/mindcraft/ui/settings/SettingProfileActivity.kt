package com.thepetot.mindcraft.ui.settings

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.pref.UserModel
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingProfileActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference
    private lateinit var profileImageView: ImageView
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var photoButton: Button
    private lateinit var saveButton: Button

    private val IMAGE_PICK_REQUEST_CODE = 1000  // Unique request code for image picking

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_profile) // Update with your layout file name

        // Bind UI elements
        profileImageView = findViewById(R.id.profileImageView)
        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        photoButton = findViewById(R.id.photoButton)
        saveButton = findViewById(R.id.saveButton)

        // Initialize UserPreference
        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        // Load user data and populate the form
        loadUserData()

        // Handle button clicks
        photoButton.setOnClickListener {
            // Open gallery to pick an image
            openGallery()
        }

        saveButton.setOnClickListener {
            // Handle user data update
            updateUserData()
        }
    }

    private fun openGallery() {
        // Create an Intent to open the gallery
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"  // Filter to only show images
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
    }

    // This method handles the result from the gallery pick
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_REQUEST_CODE) {
            // Get the selected image URI
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                // Set the selected image to the ImageView
                profileImageView.setImageURI(selectedImageUri)
                // Optionally, save the URI or path for future use (e.g., save to user profile)
            }
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            // Mengambil data pengguna yang sudah diloginkan dari UserPreference
            val user = userPreference.getUserData().first()

            // Menampilkan First Name dan Last Name, dengan nilai default jika kosong
            firstNameEditText.setText(user.firstName.ifEmpty { "First Name" })
            lastNameEditText.setText(user.lastName.ifEmpty { "Last Name" })
            emailEditText.setText(user.email.ifEmpty { "email@example.com" })
            passwordEditText.setText(user.password.ifEmpty { "********" }) // Set default value for password

            // Menampilkan foto profil jika ada
            if (user.profilePicture.isNotEmpty()) {
                // Menggunakan Glide untuk memuat gambar profil
                Glide.with(this@SettingProfileActivity)
                    .load(user.profilePicture) // Asumsikan profilePicture adalah URI atau path file
                    .into(profileImageView)
            }
        }
    }


    private fun updateUserData() {
        lifecycleScope.launch {
            val user = UserModel(
                userId = userPreference.getUserData().first().userId,
                firstName = firstNameEditText.text.toString(),
                lastName = lastNameEditText.text.toString(),
                email = emailEditText.text.toString(),
                password = passwordEditText.text.toString(),
                profilePicture = "", // You can update this with the image URI or path
                is2FA = userPreference.getUserData().first().is2FA,
                accessToken = userPreference.getUserData().first().accessToken,
                refreshToken = userPreference.getUserData().first().refreshToken,
                sessionId = userPreference.getUserData().first().sessionId
            )
            userPreference.saveUserData(user)
            // Show success message or navigate back
        }
    }
}
