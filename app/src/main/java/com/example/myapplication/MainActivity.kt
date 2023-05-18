package com.example.myapplication

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var wordTextView: TextView
    private lateinit var inputEditText: EditText
    private lateinit var startButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var scoreTextView: TextView

    private lateinit var countDownTimer: CountDownTimer
    private var gameTimeMillis: Long = 60000 // 60 seconds
    private var timeLeftMillis: Long = 0
    private var gameStarted = false
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordTextView = findViewById(R.id.wordTextView)
        inputEditText = findViewById(R.id.inputEditText)
        startButton = findViewById(R.id.startButton)
        timerTextView = findViewById(R.id.timerTextView)
        scoreTextView = findViewById(R.id.scoreTextView)

        startButton.setOnClickListener { onStartButtonClicked() }
    }

    private fun onStartButtonClicked() {
        if (gameStarted) {
            resetGame()
        } else {
            startGame()
        }
    }

    private fun startGame() {
        gameStarted = true
        startButton.text = getString(R.string.reset_button_label)
        inputEditText.isEnabled = true
        inputEditText.text.clear()
        inputEditText.requestFocus()

        score = 0
        scoreTextView.text = getString(R.string.score_label, score)

        generateRandomWord()
        countDownTimer = object : CountDownTimer(gameTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                gameStarted = false
                startButton.text = getString(R.string.start_button_label)
                inputEditText.isEnabled = false
                wordTextView.text = getString(R.string.game_over_message)
            }
        }
        countDownTimer.start()
    }

    private fun resetGame() {
        countDownTimer.cancel()
        gameStarted = false
        startButton.text = getString(R.string.start_button_label)
        inputEditText.isEnabled = false
        wordTextView.text = ""
        timerTextView.text = getString(R.string.default_timer_label)
    }

    private fun generateRandomWord() {
        val words = listOf("사과", "바나나", "딸기", "오렌지", "포도")
        val randomIndex = Random.nextInt(0, words.size)
        val randomWord = words[randomIndex]
        wordTextView.text = randomWord
    }

    private fun updateTimer() {
        val seconds = (timeLeftMillis / 1000).toString()
        timerTextView.text = getString(R.string.timer_label, seconds)
    }

    fun checkWord(view: View) {
        val inputWord = inputEditText.text.toString()
        val displayedWord = wordTextView.text.toString()

        if (inputWord.isEmpty()) {
            return
        }

        if (inputWord.equals(displayedWord, ignoreCase = true)) {
            score++
            scoreTextView.text = getString(R.string.score_label, score)
            generateRandomWord()
        } else {
            showWrongWordDialog()
        }

        inputEditText.text.clear()
    }

    private fun showWrongWordDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.wrong_word_dialog_title)
        alertDialogBuilder.setMessage(R.string.wrong_word_dialog_message)
        alertDialogBuilder.setPositiveButton(R.string.wrong_word_dialog_button) { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}