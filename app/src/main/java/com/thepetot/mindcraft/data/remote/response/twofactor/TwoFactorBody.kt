package com.thepetot.mindcraft.data.remote.response.twofactor

import com.google.gson.annotations.SerializedName

data class TwoFactorBody(

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("secret")
	val secret: String,

	@field:SerializedName("token")
	val token: String
)
