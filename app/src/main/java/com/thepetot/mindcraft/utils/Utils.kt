package com.thepetot.mindcraft.utils

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.viewpager2.widget.ViewPager2
import com.thepetot.mindcraft.BuildConfig
import com.thepetot.mindcraft.data.dummy.RankingUserModel
import com.thepetot.mindcraft.data.dummy.SearchHistoryModel
import com.thepetot.mindcraft.data.remote.response.ListQuestionsItem
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.data.remote.response.Options
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

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

fun generateDummyQuizItems(): List<ListQuizItem> {
    return listOf(
        ListQuizItem(
            duration = 5,
            createdAt = "2024-01-01T08:00:00.000Z",
            question = 5,
            author = "Admin",
            description = "What is the capital of France?",
            id = "1",
            title = "Capital Quiz"
        ),
        ListQuizItem(
            duration = 900,
            createdAt = "2024-01-01T08:05:00.000Z",
            question = 5,
            author = "Admin",
            description = "Which planet is known as the Red Planet?",
            id = "2",
            title = "Planetary Quiz"
        ),
        ListQuizItem(
            duration = 900,
            createdAt = "2024-01-01T08:10:00.000Z",
            question = 5,
            author = "Admin",
            description = "What is the largest mammal in the world?",
            id = "3",
            title = "Animal Quiz"
        ),
        ListQuizItem(
            duration = 900,
            createdAt = "2024-01-01T08:15:00.000Z",
            question = 5,
            author = "Admin",
            description = "What is the chemical symbol for water?",
            id = "4",
            title = "Chemistry Quiz"
        ),
        ListQuizItem(
            duration = 900,
            createdAt = "2024-01-01T08:20:00.000Z",
            question = 5,
            author = "Admin",
            description = "Who wrote 'Romeo and Juliet'?",
            id = "5",
            title = "Literature Quiz"
        ),
        ListQuizItem(
            duration = 900,
            createdAt = "2024-01-01T08:20:00.000Z",
            question = 5,
            author = "Admin",
            description = "Who wrote 'Romeo and Juliet'?",
            id = "5",
            title = "Literature Quiz"
        ),
        ListQuizItem(
            duration = 900,
            createdAt = "2024-01-01T08:20:00.000Z",
            question = 5,
            author = "Admin",
            description = "Who wrote 'Romeo and Juliet'?",
            id = "5",
            title = "Literature Quiz"
        ),
        ListQuizItem(
            duration = 900,
            createdAt = "2024-01-01T08:20:00.000Z",
            question = 5,
            author = "Admin",
            description = "Who wrote 'Romeo and Juliet'?",
            id = "5",
            title = "Literature Quiz"
        ),
        ListQuizItem(
            duration = 900,
            createdAt = "2024-01-01T08:20:00.000Z",
            question = 5,
            author = "Admin",
            description = "Who wrote 'Romeo and Juliet'?",
            id = "5",
            title = "Literature Quiz"
        )
    )
}

fun generateDummyQuestions(): List<ListQuestionsItem> {
    return listOf(
        ListQuestionsItem(
            question = "What is the capital of France?",
            options = Options(
                a = "Paris",
                b = "London",
                c = "Berlin",
                d = "Madrid"
            ),
            correctAnswer = "A",
            explanation = "Paris is the capital of France."
        ),
        ListQuestionsItem(
            question = "Which planet is known as the Red Planet?",
            options = Options(
                a = "Earth",
                b = "Mars",
                c = "Jupiter",
                d = "Venus"
            ),
            correctAnswer = "B",
            explanation = "Mars is known as the Red Planet because of its reddish appearance."
        ),
        ListQuestionsItem(
            question = "What is the largest mammal in the world?",
            options = Options(
                a = "Elephant",
                b = "Whale Shark",
                c = "Blue Whale",
                d = "Giraffe"
            ),
            correctAnswer = "C",
            explanation = "The Blue Whale is the largest mammal in the world."
        ),
        ListQuestionsItem(
            question = "What is the chemical symbol for water?",
            options = Options(
                a = "O2",
                b = "H2",
                c = "H2O",
                d = "HO"
            ),
            correctAnswer = "C",
            explanation = "H2O is the chemical formula for water."
        ),
        ListQuestionsItem(
            question = "Who wrote 'Romeo and Juliet'?",
            options = Options(
                a = "Charles Dickens",
                b = "William Shakespeare",
                c = "Mark Twain",
                d = "Jane Austen"
            ),
            correctAnswer = "B",
            explanation = "William Shakespeare is the author of 'Romeo and Juliet.'"
        )
    )
}

fun generateSearchHistoryDummy(): List<SearchHistoryModel> {
    return listOf(
        SearchHistoryModel("Soal MTK"),
        SearchHistoryModel("Soal IPA"),
        SearchHistoryModel("Soal IPS"),
        SearchHistoryModel("Soal Bahasa Inggris"),
        SearchHistoryModel("Soal Bahasa Indonesia"),
        SearchHistoryModel("Soal Informatika"),
    )
}

fun generateRankingUsers(): List<RankingUserModel> {
    val firstNames = listOf(
        "Alice", "Bob", "Charlie", "David", "Emma",
        "Fiona", "George", "Hannah", "Isaac", "Julia",
        "Kevin", "Lily", "Michael", "Nina", "Oscar"
    )

    val profilePictures = listOf(
        "https://example.com/profile1.png",
        "https://example.com/profile2.png",
        "https://example.com/profile3.png",
        "https://example.com/profile4.png",
        "https://example.com/profile5.png"
    )

    return List(15) { index ->
        RankingUserModel(
            rank = index + 1,
            firstName = firstNames.random(),
            profilePicture = profilePictures.random(),
            score = Random.nextInt(50, 500)
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

fun formatSecondsToTimer(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
}

fun String.withDateFormat(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date = format.parse(this)
    return DateFormat.getDateInstance(DateFormat.FULL).format(date!!)
}

fun ViewPager2.setCurrentItem(
    item: Int,
    duration: Long,
    interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
    pagePxWidth: Int = width // Default value taken from getWidth() from ViewPager2 view
) {
    val pxToDrag: Int = pagePxWidth * (item - currentItem)
    val animator = ValueAnimator.ofInt(0, pxToDrag)
    var previousValue = 0
    animator.addUpdateListener { valueAnimator ->
        val currentValue = valueAnimator.animatedValue as Int
        val currentPxToDrag = (currentValue - previousValue).toFloat()
        fakeDragBy(-currentPxToDrag)
        previousValue = currentValue
    }
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) { beginFakeDrag() }
        override fun onAnimationEnd(animation: Animator) { endFakeDrag() }
        override fun onAnimationCancel(animation: Animator) { /* Ignored */ }
        override fun onAnimationRepeat(animation: Animator) { /* Ignored */ }
    })
    animator.interpolator = interpolator
    animator.duration = duration
    animator.start()
}

fun getBitmapFromBase64(base64String: String): Bitmap {
    val decodedString: ByteArray =
        Base64.decode(base64String.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1], Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}

fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    return try {
        // Remove any "data:image/png;base64," prefix if present
        val cleanBase64 = base64String.replace("data:image/png;base64,", "")

        // Decode the Base64 string into a byte array
        val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

        // Convert the byte array into a Bitmap
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }
}

fun logMessage(
    logFunction: (String, String, Throwable?) -> Int,
    tag: String,
    message: String,
    throwable: Throwable? = null
) {
    if (BuildConfig.DEBUG) {
        logFunction(tag, message, throwable)
    }
}

fun logMessage(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.i(tag, message)
    }
}