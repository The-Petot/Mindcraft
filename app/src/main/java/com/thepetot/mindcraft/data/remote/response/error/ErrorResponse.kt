package com.thepetot.mindcraft.data.remote.response.error

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ErrorResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("errors")
	val errors: List<ErrorsItem>
) : Parcelable