package com.thepetot.mindcraft.data.pref

data class UserModel(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val profilePicture: String,
    val is2FA: Boolean,
    val accessToken: String,
    val refreshToken: String,
    val sessionId: String
)