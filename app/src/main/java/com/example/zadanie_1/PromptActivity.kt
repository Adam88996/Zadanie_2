package com.example.zadanie_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PromptActivity : AppCompatActivity() {

    companion object {
        const val KEY_EXTRA_ANSWER_SHOWN = "pb.edu.pl.wi.quiz.answerShown"
    }

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var backButton: Button
    private var correctAnswer = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prompt)


        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        backButton = findViewById(R.id.back_button)


        correctAnswer = intent.getBooleanExtra(
            MainActivity.KEY_EXTRA_ANSWER,
            true
        )


        showAnswerButton.setOnClickListener {
            val answerText = if (correctAnswer) {
                getString(R.string.button_true)
            } else {
                getString(R.string.button_false)
            }
            answerTextView.text = answerText
            setAnswerShownResult(true)
        }


        backButton.setOnClickListener {
            finish()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setAnswerShownResult(answerWasShown: Boolean) {
        val resultIntent = Intent()
        resultIntent.putExtra(KEY_EXTRA_ANSWER_SHOWN, answerWasShown)
        setResult(RESULT_OK, resultIntent)
    }
}