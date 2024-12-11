package com.thepetot.mindcraft.data.remote.response.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("links")
	val links: Links,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: List<ErrorsItem>
)

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
	val updatedAt: String,

	@field:SerializedName("accessToken")
	var accessToken: String,

	@field:SerializedName("refreshToken")
	var refreshToken: String,

	@field:SerializedName("sessionId")
	var sessionId: String
)

data class ErrorsItem(

	@field:SerializedName("field")
	val field: String,

	@field:SerializedName("messages")
	val messages: List<String>
)

data class Links(

	@field:SerializedName("logout")
	val logout: String,

	@field:SerializedName("tokenRefresh")
	val tokenRefresh: String,

	@field:SerializedName("self")
	val self: String,

	@field:SerializedName("toggleTwoFactorAuth")
	val toggleTwoFactorAuth: String
)
