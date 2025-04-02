package com.example.mycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mycalculator.ui.theme.MyCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyCalculatorTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }
    var operation by remember { mutableStateOf("") }
    var num1 by remember { mutableStateOf<Float?>(null) }
    var num2 by remember { mutableStateOf<Float?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    fun calculate() {
        if (num1 == null || num2 == null || operation.isEmpty()) {
            errorMessage = "Invalid Input"
            return
        }
        result = try {
            when (operation) {
                "+" -> (num1!! + num2!!).toString()
                "-" -> (num1!! - num2!!).toString()
                "x" -> (num1!! * num2!!).toString()
                "/" -> if (num2 == 0f) "Cannot divide by zero" else (num1!! / num2!!).toString()
                "%" -> (num1!! % num2!!).toString()
                else -> ""
            }
        } catch (e: Exception) {
            "Error"
        }
        num1 = null
        num2 = null
        operation = ""
        input = result
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "My Calculator App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = input,
            onValueChange = { input = it },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Result: $result", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        val buttons = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "x"),
            listOf("1", "2", "3", "-"),
            listOf("0", "%", "C", "+"),
            listOf("=")
        )

        buttons.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { label ->
                    Button(
                        onClick = {
                            when (label) {
                                "C" -> {
                                    input = ""
                                    result = "0"
                                    errorMessage = ""
                                    num1 = null
                                    num2 = null
                                    operation = ""
                                }
                                "+", "-", "x", "/", "%" -> {
                                    if (input.isNotEmpty()) {
                                        num1 = input.toFloat()
                                        operation = label
                                        input = ""
                                    }
                                }
                                "=" -> {
                                    if (input.isNotEmpty() && num1 != null) {
                                        num2 = input.toFloat()
                                        calculate()
                                    }
                                }
                                else -> input += label
                            }
                        },
                        modifier = Modifier.weight(1f).padding(4.dp)
                    ) {
                        Text(label)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyCalculatorTheme {
        CalculatorApp()
    }
}