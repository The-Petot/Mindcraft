package com.thepetot.mindcraft.data.remote.response.login

import com.google.gson.annotations.SerializedName

data class OAuthLoginBody(

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("twoFAToken")
	val twoFAToken: String? = null
)
