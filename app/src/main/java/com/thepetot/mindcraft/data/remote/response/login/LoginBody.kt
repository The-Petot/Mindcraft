package com.thepetot.mindcraft.data.remote.response.login

import com.google.gson.annotations.SerializedName

data class LoginBody(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("token")
	val token: String? = null
)
