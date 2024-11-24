package com.thepetot.mindcraft.utils

import com.thepetot.mindcraft.data.remote.response.ListQuizItem

fun generateDummyData(count: Int): List<ListQuizItem> {
    return List(count) { index ->
        ListQuizItem(
            duration = (10..60).random(), // Random duration between 10 and 60 minutes
            createdAt = "2024-11-${(20..24).random()}T${(0..23).random()}:${(0..59).random()}:${(0..59).random()}", // Random date
            question = (5..20).random(), // Random number of questions
            author = "Author $index", // Author name with index
            description = "Description for quiz item $index", // Unique description
            id = "id_$index", // Unique ID
            title = "Quiz Title $index" // Unique title
        )
    }
}