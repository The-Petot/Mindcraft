package com.thepetot.mindcraft.data.remote.response.auth.logout

import com.google.gson.annotations.SerializedName

data class Links(

	@field:SerializedName("tokenRefresh")
	val tokenRefresh: String,

	@field:SerializedName("self")
	val self: String,

	@field:SerializedName("getTwoFactorQR")
	val getTwoFactorQR: String,

	@field:SerializedName("toggleTwoFactorAuth")
	val toggleTwoFactorAuth: String,

	@field:SerializedName("login")
	val login: String
)