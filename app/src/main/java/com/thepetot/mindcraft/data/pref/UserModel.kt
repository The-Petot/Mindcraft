package com.thepetot.mindcraft.data.pref

data class UserModel(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val isLogin: Boolean,
    val userId: Int? = null // Tambahkan userId di sini
)