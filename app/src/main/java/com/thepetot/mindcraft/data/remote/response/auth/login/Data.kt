package com.thepetot.mindcraft.data.remote.response.auth.login

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("currentRank")
	val currentRank: Int,

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("lastName")
	val lastName: String,

	@field:SerializedName("twoFactorEnabled")
	val twoFactorEnabled: Boolean,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("profileImgUrl")
	val profileImgUrl: String,

	@field:SerializedName("twoFactorSecret")
	val twoFactorSecret: String? = null,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("totalScore")
	val totalScore: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("notificationEnabled")
	val notificationEnabled: Boolean,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)