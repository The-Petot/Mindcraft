package com.thepetot.mindcraft.data.remote.response.auth.signup

import com.google.gson.annotations.SerializedName

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