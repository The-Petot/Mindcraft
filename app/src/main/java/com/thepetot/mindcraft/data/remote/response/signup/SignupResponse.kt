package com.thepetot.mindcraft.data.remote.response.signup

import com.google.gson.annotations.SerializedName

data class SignupResponse(

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

data class Links(

	@field:SerializedName("logout")
	val logout: String,

	@field:SerializedName("tokenRefresh")
	val tokenRefresh: String,

	@field:SerializedName("self")
	val self: String,

	@field:SerializedName("toggleTwoFactorAuth")
	val toggleTwoFactorAuth: String,

	@field:SerializedName("login")
	val login: String
)

data class ErrorsItem(

	@field:SerializedName("field")
	val field: String,

	@field:SerializedName("messages")
	val messages: List<String>
)

data class Data(

	@field:SerializedName("userId")
	val userId: Int
)
