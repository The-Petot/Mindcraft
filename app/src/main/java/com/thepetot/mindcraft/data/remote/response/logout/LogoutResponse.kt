package com.thepetot.mindcraft.data.remote.response.logout

import com.google.gson.annotations.SerializedName

data class LogoutResponse(

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

	@field:SerializedName("enableTwoFactorAuth")
	val enableTwoFactorAuth: String,

	@field:SerializedName("tokenRefresh")
	val tokenRefresh: String,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("register")
	val register: String
)

data class ErrorsItem(

	@field:SerializedName("field")
	val field: String,

	@field:SerializedName("messages")
	val messages: List<String>
)
