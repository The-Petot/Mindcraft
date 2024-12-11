package com.thepetot.mindcraft.data.remote.body.auth

import com.google.gson.annotations.SerializedName

data class LogoutBody(

	@field:SerializedName("userId")
	val userId: Int
)