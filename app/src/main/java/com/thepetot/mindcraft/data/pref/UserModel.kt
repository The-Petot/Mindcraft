package com.thepetot.mindcraft.data.pref

data class UserModel(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val totalScore: Int,
    val currentRank: Int,
    val profilePicture: String,
    val twoFactorEnable: Boolean,
    val twoFactorSecret: String?,
    val notificationEnabled: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val accessToken: String,
    val refreshToken: String,
    val sessionId: String
)