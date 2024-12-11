package com.thepetot.mindcraft.data.remote.response.auth.login

import com.google.gson.annotations.SerializedName

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