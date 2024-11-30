package com.thepetot.mindcraft.ui.settings

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.thepetot.mindcraft.databinding.ActivitySettingsProfileBinding

class SettingsProfileActivity : AppCompatActivity() {

//    private lateinit var userPreference: UserPreference
//    private lateinit var profileImageView: ImageView
//    private lateinit var firstNameEditText: EditText
//    private lateinit var lastNameEditText: EditText
//    private lateinit var emailEditText: EditText
//    private lateinit var passwordEditText: EditText
//    private lateinit var photoButton: Button
//    private lateinit var saveButton: Button

//    private val IMAGE_PICK_REQUEST_CODE = 1000  // Unique request code for image picking

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
            settingsBottomSheetPassword.show(supportFragmentManager, SettingsBottomSheetPassword.TAG)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Bind UI elements
//        profileImageView = findViewById(R.id.img_profile)
//        firstNameEditText = findViewById(R.id.firstNameEditText)
//        lastNameEditText = findViewById(R.id.lastNameEditText)
//        emailEditText = findViewById(R.id.emailEditText)
//        passwordEditText = findViewById(R.id.passwordEditText)
//        photoButton = findViewById(R.id.photoButton)
//        saveButton = findViewById(R.id.saveButton)
//
//        // Initialize UserPreference
//        userPreference = UserPreference.getInstance(applicationContext.dataStore)
//
//        // Load user data and populate the form
//        loadUserData()
//
//        // Handle button clicks
////        photoButton.setOnClickListener {
////            // Open gallery to pick an image
////            openGallery()
////        }
//
//        saveButton.setOnClickListener {
//            // Handle user data update
//            updateUserData()
//        }
//
//        profileImageView.setOnClickListener {
//            println("DDDDDDDDDDDAWDAWDAWDAWDAWDAWD")
//        }
    }

//    private fun openGallery() {
//        // Create an Intent to open the gallery
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        intent.type = "image/*"  // Filter to only show images
//        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
//    }
//
//    // This method handles the result from the gallery pick
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_REQUEST_CODE) {
//            // Get the selected image URI
//            val selectedImageUri: Uri? = data?.data
//            if (selectedImageUri != null) {
//                // Set the selected image to the ImageView
//                profileImageView.setImageURI(selectedImageUri)
//                // Optionally, save the URI or path for future use (e.g., save to user profile)
//            }
//        }
//    }
//
//    private fun loadUserData() {
//        lifecycleScope.launch {
//            // Mengambil data pengguna yang sudah diloginkan dari UserPreference
//            val user = userPreference.getUserData().first()
//
//            // Menampilkan First Name dan Last Name, dengan nilai default jika kosong
//            firstNameEditText.setText(user.firstName.ifEmpty { "First Name" })
//            lastNameEditText.setText(user.lastName.ifEmpty { "Last Name" })
//            emailEditText.setText(user.email.ifEmpty { "email@example.com" })
//            passwordEditText.setText(user.password.ifEmpty { "********" }) // Set default value for password
//
//            // Menampilkan foto profil jika ada
//            if (user.profilePicture.isNotEmpty()) {
//                // Menggunakan Glide untuk memuat gambar profil
//                Glide.with(this@SettingProfileActivity)
//                    .load(user.profilePicture) // Asumsikan profilePicture adalah URI atau path file
//                    .into(profileImageView)
//            }
//        }
//    }
//
//
//    private fun updateUserData() {
//        lifecycleScope.launch {
//            val user = UserModel(
//                userId = userPreference.getUserData().first().userId,
//                firstName = firstNameEditText.text.toString(),
//                lastName = lastNameEditText.text.toString(),
//                email = emailEditText.text.toString(),
//                password = passwordEditText.text.toString(),
//                profilePicture = "", // You can update this with the image URI or path
//                is2FA = userPreference.getUserData().first().is2FA,
//                accessToken = userPreference.getUserData().first().accessToken,
//                refreshToken = userPreference.getUserData().first().refreshToken,
//                sessionId = userPreference.getUserData().first().sessionId
//            )
//            userPreference.saveUserData(user)
//            // Show success message or navigate back
//        }
//    }
}
