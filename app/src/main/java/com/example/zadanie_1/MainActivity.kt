package com.example.zadanie_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var promptButton: Button

    private val questions: Array<Question> = arrayOf(
        Question(R.string.q1, true),
        Question(R.string.q2, false),
        Question(R.string.q3, false),
        Question(R.string.q4, true),
        Question(R.string.q5, true)
    )

    private var currentQuestionIndex = 0
    private var correctAnswersCount = 0
    private var totalQuestions = questions.size
    private var answerWasShown = false
    private var isLastQuestion = false
    private var areButtonsEnabled = true

    companion object {
        const val QUIZ_TAG = "MainActivity"
        const val KEY_CURRENT_INDEX = "currentIndex"
        const val KEY_IS_LAST_QUESTION = "isLastQuestion"
        const val KEY_BUTTONS_ENABLED = "buttonsEnabled"
        const val KEY_EXTRA_ANSWER = "pl.edu.pb.wi.quiz.correctAnswer"
        const val REQUEST_CODE_PROMPT = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        Log.d(QUIZ_TAG, "Wywołana została metoda cyklu życia: onCreate")

        // Odczytanie zapisanego stanu
        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, 0)
            isLastQuestion = savedInstanceState.getBoolean(KEY_IS_LAST_QUESTION, false)
            areButtonsEnabled = savedInstanceState.getBoolean(KEY_BUTTONS_ENABLED, true)
        }

        initViews()
        setNextQuestion()
        updateButtonsState()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        questionTextView = findViewById(R.id.question_text_view)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        promptButton = findViewById(R.id.prompt_button)

        trueButton.setOnClickListener {
            checkAnswerCorrectness(true)
        }

        falseButton.setOnClickListener {
            checkAnswerCorrectness(false)
        }

        nextButton.setOnClickListener {
            if (isLastQuestion) {
                restartQuiz()
            } else {
                currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
                answerWasShown = false
                setNextQuestion()
            }
        }

        promptButton.setOnClickListener {
            val intent = Intent(this, PromptActivity::class.java)
            val correctAnswer = questions[currentQuestionIndex].trueAnswer
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer)
            startActivityForResult(intent, REQUEST_CODE_PROMPT)
        }
    }

    private fun setNextQuestion() {
        questionTextView.setText(questions[currentQuestionIndex].questionId)

        isLastQuestion = (currentQuestionIndex == totalQuestions - 1)

        if (isLastQuestion) {
            nextButton.text = getString(R.string.button_restart)
        } else {
            nextButton.text = getString(R.string.button_next)
        }
    }

    private fun updateButtonsState() {
        trueButton.isEnabled = areButtonsEnabled
        falseButton.isEnabled = areButtonsEnabled
    }

    private fun checkAnswerCorrectness(userAnswer: Boolean) {
        val correctAnswer = questions[currentQuestionIndex].trueAnswer
        val resultMessageId = when {
            answerWasShown -> R.string.answer_was_shown
            userAnswer == correctAnswer -> {
                correctAnswersCount++
                R.string.correct_answer
            }
            else -> R.string.incorrect_answer
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show()

        if (currentQuestionIndex == totalQuestions - 1 && userAnswer == correctAnswer) {
            showFinalScore()
        }
    }

    private fun showFinalScore() {
        val scoreMessage = "Quiz zakończony! Wynik: $correctAnswersCount/$totalQuestions"
        Toast.makeText(this, scoreMessage, Toast.LENGTH_LONG).show()

        areButtonsEnabled = false
        updateButtonsState()
        nextButton.text = getString(R.string.button_restart)
        isLastQuestion = true
    }

    private fun restartQuiz() {
        currentQuestionIndex = 0
        correctAnswersCount = 0
        answerWasShown = false
        isLastQuestion = false
        areButtonsEnabled = true
        setNextQuestion()
        updateButtonsState()

        trueButton.isEnabled = true
        falseButton.isEnabled = true
        nextButton.text = getString(R.string.button_next)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) return
        if (requestCode == REQUEST_CODE_PROMPT) {
            data?.let {
                answerWasShown = it.getBooleanExtra(
                    PromptActivity.KEY_EXTRA_ANSWER_SHOWN,
                    false
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(QUIZ_TAG, "Wywołana została metoda: onSaveInstanceState")
        outState.putInt(KEY_CURRENT_INDEX, currentQuestionIndex)
        outState.putBoolean(KEY_IS_LAST_QUESTION, isLastQuestion)
        outState.putBoolean(KEY_BUTTONS_ENABLED, areButtonsEnabled)
    }

    override fun onStart() {
        super.onStart()
        Log.d(QUIZ_TAG, "Wywołana została metoda cyklu życia: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(QUIZ_TAG, "Wywołana została metoda cyklu życia: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(QUIZ_TAG, "Wywołana została metoda cyklu życia: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(QUIZ_TAG, "Wywołana została metoda cyklu życia: onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(QUIZ_TAG, "Wywołana została metoda cyklu życia: onDestroy")
    }
}