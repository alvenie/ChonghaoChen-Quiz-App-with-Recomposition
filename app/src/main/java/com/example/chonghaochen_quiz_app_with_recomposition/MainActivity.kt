package com.example.chonghaochen_quiz_app_with_recomposition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCardsQuizApp()
        }
    }
}

@Composable
fun FlashCardsQuizApp() {
    val flashcardData = remember {
        listOf(
            mapOf("question" to "What is the capital of France?", "answer" to "Paris"),
            mapOf("question" to "What is 2 + 2?", "answer" to "4"),
            mapOf("question" to "What is the chemical symbol for water?", "answer" to "H2O"),
            mapOf("question" to "Who wrote \"Romeo and Juliet\"?", "answer" to "Shakespeare"),
            mapOf("question" to "What is the largest planet in our solar system?", "answer" to "Jupiter")
        )
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var isQuizComplete by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!isQuizComplete) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = flashcardData[currentQuestionIndex]["question"] as String,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                OutlinedTextField(
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    label = { Text("Your Answer") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )

                Button(
                    onClick = {
                        val correctAnswer = flashcardData[currentQuestionIndex]["answer"] as String
                        if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
                            snackbarMessage = "Correct!"
                            currentQuestionIndex++
                            if (currentQuestionIndex >= flashcardData.size) {
                                isQuizComplete = true
                                snackbarMessage = "Quiz complete! Great job!"
                            }
                        } else {
                            snackbarMessage = "Incorrect. Try again!"
                        }
                        showSnackbar = true
                        userAnswer = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit Answer")
                }
            } else {
                Text("Quiz Complete!")
                Button(
                    onClick = {
                        currentQuestionIndex = 0
                        isQuizComplete = false
                        snackbarMessage = "Quiz restarted!"
                        showSnackbar = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Restart Quiz")
                }
            }
        }
    }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(snackbarMessage)
            showSnackbar = false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlashCardsQuizApp()
}