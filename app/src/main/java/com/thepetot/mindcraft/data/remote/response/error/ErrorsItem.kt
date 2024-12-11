package com.thepetot.mindcraft.data.remote.response.error

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ErrorsItem(

	@field:SerializedName("field")
	val field: String? = null,

	@field:SerializedName("messages")
	val messages: List<String>
) : Parcelable