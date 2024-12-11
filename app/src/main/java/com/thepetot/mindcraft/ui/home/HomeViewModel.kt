package com.thepetot.mindcraft.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.data.remote.response.challenges.test.DataItem
import com.thepetot.mindcraft.data.repository.ChallengesRepository
import com.thepetot.mindcraft.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(
    private val challengesRepository: ChallengesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    var quizHistory: List<ListQuizItem> = listOf()
    var searchState: Boolean = false

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> get() = _searchQuery

//    val challenges: LiveData<PagingData<DataItem>> =
//        challengesRepository.getChallenges().cachedIn(viewModelScope)

    val challenges: LiveData<PagingData<DataItem>> = searchQuery.switchMap { query ->
        challengesRepository.getChallenges(query).cachedIn(viewModelScope)
    }

    fun refreshToken() {
        viewModelScope.launch {
            val userId = userRepository.getPreferenceSettings(UserPreference.USERID_KEY, 0).first()
            userRepository.refreshToken(userId)
        }
    }

    fun insertSearchQuery(query: String) {
        viewModelScope.launch {
            challengesRepository.insertSearchQuery(query)
        }
    }

    fun getSearchQueries() = challengesRepository.getSearchQueries()

    fun updateSearchQuery(query: String? = null) {
        _searchQuery.value = query
    }
}