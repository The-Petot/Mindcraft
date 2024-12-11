package com.thepetot.mindcraft.ui.home.quiz.add

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thepetot.mindcraft.data.remote.body.users.CreateParticipationBody
import com.thepetot.mindcraft.data.remote.response.challenges.CreateChallengeBody
import com.thepetot.mindcraft.data.remote.response.challenges.CreateChallengeResponse
import com.thepetot.mindcraft.data.remote.response.participations.CreateParticipationsResponse
import com.thepetot.mindcraft.data.remote.response.user.UserResponse
import com.thepetot.mindcraft.data.remote.response.user.all_users.GetAllUsersResponse
import com.thepetot.mindcraft.data.repository.UserRepository
import com.thepetot.mindcraft.utils.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddQuizViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _createQuizResult = MediatorLiveData<Result<CreateChallengeResponse>?>()
    val createQuizResult: LiveData<Result<CreateChallengeResponse>?> get() = _createQuizResult

    fun createQuiz(userId: Int, title: String, description: String, material: String, timeSeconds: Int, tags: List<String> = emptyList()) {
        val createChallengeBody = CreateChallengeBody(timeSeconds, material, description, title, tags)
        val source = userRepository.createQuiz(userId, createChallengeBody)
        _createQuizResult.addSource(source) { result ->
            _createQuizResult.value = result
            if (result is Result.Success || result is Result.Error) {
                _createQuizResult.removeSource(source)
            }
        }
    }

    fun clearCreateQuiz() {
        _createQuizResult.value = null
    }

    fun <T> getPreferenceSettings(pref: Preferences.Key<T>, defaultValue: T) = runBlocking {
        userRepository.getPreferenceSettings(pref, defaultValue).first()
    }

    fun <T> setPreferenceSettings(pref: Preferences.Key<T>, value: T) {
        viewModelScope.launch {
            userRepository.setPreferenceSettings(pref, value)
        }
    }

    fun createParticipations(userId: Int, challengeId: Int, score: Int): LiveData<Result<CreateParticipationsResponse>> {
        val createParticipationBody = CreateParticipationBody(score, challengeId)
        return userRepository.createParticipations(userId, createParticipationBody)
    }

    fun getAllUsers(): LiveData<Result<GetAllUsersResponse>> = userRepository.getAllUsers()

    fun getUserById(userId: Int): LiveData<Result<UserResponse>> = userRepository.getUserById(userId)

//    fun login(email: String, password: String) {
//        val loginBody = LoginBody(password, email, null)
//        val source = userRepository.login(loginBody)
//        _loginResult.addSource(source) { result ->
//            _loginResult.value = result
//
//            // Remove the source when the operation is complete
//            if (result is Result.Success || result is Result.Error) {
//                _loginResult.removeSource(source)
//            }
//        }
//    }
}