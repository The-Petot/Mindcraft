package com.thepetot.mindcraft.data.remote.body.auth

import com.google.gson.annotations.SerializedName

data class OAuthBody(

	@field:SerializedName("twoFAToken")
	val twoFAToken: String? = null,

	@field:SerializedName("token")
	val token: String
)