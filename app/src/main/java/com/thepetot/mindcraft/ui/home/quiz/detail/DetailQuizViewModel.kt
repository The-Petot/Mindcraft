package com.thepetot.mindcraft.ui.home.quiz.detail

import androidx.lifecycle.ViewModel
import com.thepetot.mindcraft.data.repository.ChallengesRepository

class DetailQuizViewModel(private val challengesRepository: ChallengesRepository) : ViewModel() {
    fun getQuestionsByChallengeId(challengeId: Int) = challengesRepository.getQuestionsByChallengeId(challengeId)
}