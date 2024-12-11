package com.thepetot.mindcraft.data.remote.body.auth

import com.google.gson.annotations.SerializedName

data class SignUpBody(

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("lastName")
	val lastName: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("email")
	val email: String
)