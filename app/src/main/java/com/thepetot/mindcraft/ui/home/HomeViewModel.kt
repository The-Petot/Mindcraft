package com.thepetot.mindcraft.ui.home

import androidx.lifecycle.ViewModel
import com.thepetot.mindcraft.data.remote.response.ListQuizItem

class HomeViewModel : ViewModel() {
    var quizHistory: List<ListQuizItem> = listOf()
    var searchState: Boolean = false
}