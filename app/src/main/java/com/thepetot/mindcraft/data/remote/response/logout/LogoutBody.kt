package com.thepetot.mindcraft.data.remote.response.logout

import com.google.gson.annotations.SerializedName

data class LogoutBody(

	@field:SerializedName("userId")
	val userId: Int
)
