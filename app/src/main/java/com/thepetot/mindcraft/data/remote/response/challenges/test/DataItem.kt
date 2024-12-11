package com.thepetot.mindcraft.data.remote.response.challenges.test

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Parcelize
@Entity(tableName = "challenges")
data class DataItem(

	@field:SerializedName("summary")
	val summary: String,

	@field:SerializedName("totalQuestions")
	val totalQuestions: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("timeSeconds")
	val timeSeconds: Int,

	@field:SerializedName("description")
	val description: String,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("authorId")
	val authorId: Int,

	@field:SerializedName("tags")
	val tags: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String,

	@field:SerializedName("authorFirstName")
	val authorFirstName: String,

	@field:SerializedName("authorLastName")
	val authorLastName: String
) : Parcelable