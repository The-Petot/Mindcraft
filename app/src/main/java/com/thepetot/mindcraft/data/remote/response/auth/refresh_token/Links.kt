package com.thepetot.mindcraft.data.remote.response.auth.refresh_token

import com.google.gson.annotations.SerializedName

data class Links(

	@field:SerializedName("logout")
	val logout: String,

	@field:SerializedName("self")
	val self: String,

	@field:SerializedName("toggleTwoFactorAuth")
	val toggleTwoFactorAuth: String,

	@field:SerializedName("login")
	val login: String
)