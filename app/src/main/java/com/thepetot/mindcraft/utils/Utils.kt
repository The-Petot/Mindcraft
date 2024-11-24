package com.thepetot.mindcraft.utils

import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun generateDummyData(count: Int): List<ListQuizItem> {

    var hours = (0..23).random().toString()
    var minutes = (0..59).random().toString()
    var seconds = (0..59).random().toString()

    if (hours.toInt() < 10) hours = "0${hours}"
    if (minutes.toInt() < 10) minutes = "0${minutes}"
    if (seconds.toInt() < 10) seconds = "0${seconds}"

    return List(count) { index ->
        ListQuizItem(
            duration = (10..900).random(), // Random duration between 10 and 60 minutes
            createdAt = "2024-11-${(20..24).random()}T${hours}:${minutes}:${seconds}", // Random date
            question = (5..20).random(), // Random number of questions
            author = "Author $index", // Author name with index
            description = "Description for quiz item $index", // Unique description
            id = "id_$index", // Unique ID
            title = "Quiz Title $index" // Unique title
        )
    }
}

fun formatDuration(duration: Int): String {
    return if (duration < 60) {
        "$duration seconds"
    } else {
        "${duration / 60} minutes"
    }
}

fun String.withDateFormat(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date = format.parse(this)
    return DateFormat.getDateInstance(DateFormat.FULL).format(date!!)
}