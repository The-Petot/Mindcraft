package com.thepetot.mindcraft.data.remote.response.challenges

import com.google.gson.annotations.SerializedName

data class ChallengesResponse(
    @SerializedName("success") val success: Boolean,

    @SerializedName("data") val data: ChallengeData?,

    @SerializedName("message") val message: String,

    @SerializedName("links") val links: ChallengeLinks?

)

data class ChallengeData(
    @SerializedName("id") val id: Int,

    @SerializedName("title") val title: String,

    @SerializedName("description") val description: String,

    @SerializedName("summary") val summary: String,

    @SerializedName("tags") val tags: String,

    @SerializedName("authorId") val authorId: Int,

    @SerializedName("totalQuestions") val totalQuestions: Int,

    @SerializedName("timeSeconds") val timeSeconds: Int,

    @SerializedName("createdAt") val createdAt: String,

    @SerializedName("updatedAt") val updatedAt: String

)

data class ChallengeLinks(
    @SerializedName("self") val self: String,

    @SerializedName("challengeDetails") val challengeDetails: String,

    @SerializedName("challengeParticipants") val challengeParticipants: String,

    @SerializedName("challengeQuestions") val challengeQuestions: String
)

data class ParticipantData(
    @SerializedName("id") val id: Int,
    @SerializedName("participantId") val participantId: Int,
    @SerializedName("challengeId") val challengeId: Int,
    @SerializedName("score") val score: Int,
    @SerializedName("createdAt") val createdAt: String
)

data class ParticipantsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<ParticipantData>,
    @SerializedName("message") val message: String,
    @SerializedName("links") val links: Links
)

data class Links(
    @SerializedName("self") val self: String,
    @SerializedName("challengeDetails") val challengeDetails: String,
    @SerializedName("challengeQuestions") val challengeQuestions: String,
    @SerializedName("challenges") val challenges: String
)

data class AnswerData(
    @SerializedName("id") val id: Int,
    @SerializedName("answer") val answer: String,
    @SerializedName("questionId") val questionId: Int,
    @SerializedName("correct") val correct: Boolean,
    @SerializedName("createdAt") val createdAt: String
)

data class QuestionData(
    @SerializedName("id") val id: Int,
    @SerializedName("challengeId") val challengeId: Int,
    @SerializedName("question") val question: String,
    @SerializedName("answers") val answers: List<AnswerData>,
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("createdAt") val createdAt: String
)

data class QuestionsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<QuestionData>,
    @SerializedName("message") val message: String,
    @SerializedName("links") val links: Links
)