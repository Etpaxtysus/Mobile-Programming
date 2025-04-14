package com.example.currencyconverter

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
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConverterTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Calculator", "Currency Converter")

    Spacer(modifier = Modifier.height(16.dp))

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> CalculatorApp()
            1 -> CurrencyConverterApp()
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
        Text("My Calculator App", style = MaterialTheme.typography.headlineMedium)

        TextField(
            value = input,
            onValueChange = { input = it },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

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
                                        num1 = input.toFloatOrNull()
                                        operation = label
                                        input = ""
                                    }
                                }
                                "=" -> {
                                    if (input.isNotEmpty() && num1 != null) {
                                        num2 = input.toFloatOrNull()
                                        calculate()
                                    }
                                }
                                else -> input += label
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(label)
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyConverterApp() {
    var amountInput by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("EUR") }
    var convertedAmount by remember { mutableStateOf(0.0) }

    // Data diambil dari : https://wise.com/id/currency-converter/
    val currencyRates = mapOf(
        "USD" to 1.0,
        "IDR" to 16792.0,
        "GBP" to 0.76,
        "JPY" to 143.0,
        "EUR" to 0.88
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Currency Converter", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = amountInput,
            onValueChange = { amountInput = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuCurrency("From", fromCurrency, currencyRates.keys.toList()) {
            fromCurrency = it
        }

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuCurrency("To", toCurrency, currencyRates.keys.toList()) {
            toCurrency = it
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val amount = amountInput.toDoubleOrNull() ?: 0.0
                val fromRate = currencyRates[fromCurrency] ?: 1.0
                val toRate = currencyRates[toCurrency] ?: 1.0
                convertedAmount = amount * (toRate / fromRate)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convert")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val formattedResult = remember(convertedAmount, toCurrency) {
            val formatter = java.text.NumberFormat.getNumberInstance(java.util.Locale("in", "ID"))
            formatter.maximumFractionDigits = 2
            "${formatter.format(convertedAmount)} $toCurrency"
        }

        Text("Converted Amount: $formattedResult")

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuCurrency(
    label: String,
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = selected,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrencyConverterTheme {
        MainApp()
    }
}
