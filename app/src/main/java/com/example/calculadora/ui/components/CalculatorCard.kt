package com.example.calculadora.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

@Composable
fun CalculatorCard() {
    var displayValue by remember { mutableStateOf("0") }
    var firstOperand by remember { mutableStateOf<Double?>(null) }
    var operation by remember { mutableStateOf<String?>(null) }
    var shouldClearDisplayNext by remember { mutableStateOf(false) }

    fun onNumberClick(number: String) {
        if (shouldClearDisplayNext) {
            displayValue = number
            shouldClearDisplayNext = false
        } else {
            if (displayValue == "0" && number != ".") {
                displayValue = number
            } else if (displayValue.length < 9) {
                displayValue += number
            }
        }
    }

    fun onDecimalClick() {
        if (shouldClearDisplayNext) {
            displayValue = "0."
            shouldClearDisplayNext = false
        } else if (!displayValue.contains(".") && displayValue.length < 9) {
            displayValue += "."
        }
    }

    fun performCalculation() {
        val secondOperandString = displayValue
        if (firstOperand != null && operation != null) {
            val secondOperand = secondOperandString.toDoubleOrNull()
            if (secondOperand != null) {
                var result = 0.0
                result = when (operation) {
                    "+" -> firstOperand!! + secondOperand
                    "-" -> firstOperand!! - secondOperand
                    "*" -> firstOperand!! * secondOperand
                    "/" -> {
                        if (secondOperand == 0.0) {
                            firstOperand = null
                            operation = null
                            shouldClearDisplayNext = true
                            return
                        }
                        firstOperand!! / secondOperand
                    }
                    else -> secondOperand // Should not happen
                }
                displayValue = result.toString()
                firstOperand = result
                operation = null

            }
        }
        shouldClearDisplayNext = true
    }

    fun onOperationClick(op: String) {
        val currentDisplayNumber = displayValue.toDoubleOrNull()

        if (firstOperand != null && operation != null && !shouldClearDisplayNext && currentDisplayNumber != null) {
            performCalculation()
        }
        
        firstOperand = displayValue.toDoubleOrNull()
        operation = op
        shouldClearDisplayNext = true
    }

    fun onEqualsClick() {
        if (firstOperand != null && operation != null) {
            performCalculation()
            operation = null
        } 
    }

    fun onClearClick() {
        displayValue = "0"
        firstOperand = null
        operation = null
        shouldClearDisplayNext = false
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = displayValue,
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val buttonModifier = Modifier.weight(1f)
        val rowSpacing = 8.dp

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(rowSpacing)
        ) {
            CalculatorButton(symbol = "C", modifier = buttonModifier, onClick = ::onClearClick)
            CalculatorButton(symbol = " ", modifier = buttonModifier) { /* Botão "Placeholder" para compor UI */ }
            CalculatorButton(symbol = " ", modifier = buttonModifier) { /* Botão "Placeholder" para compor UI */ }
            CalculatorButton(symbol = "/", modifier = buttonModifier) { onOperationClick("/") }
        }
        Spacer(modifier = Modifier.height(rowSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(rowSpacing)
        ) {
            listOf("7", "8", "9").forEach { number ->
                CalculatorButton(symbol = number, modifier = buttonModifier) { onNumberClick(number) }
            }
            CalculatorButton(symbol = "*", modifier = buttonModifier) { onOperationClick("*") }
        }
        Spacer(modifier = Modifier.height(rowSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(rowSpacing)
        ) {
            listOf("4", "5", "6").forEach { number ->
                CalculatorButton(symbol = number, modifier = buttonModifier) { onNumberClick(number) }
            }
            CalculatorButton(symbol = "-", modifier = buttonModifier) { onOperationClick("-") }
        }
        Spacer(modifier = Modifier.height(rowSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(rowSpacing)
        ) {
            listOf("1", "2", "3").forEach { number ->
                CalculatorButton(symbol = number, modifier = buttonModifier) { onNumberClick(number) }
            }
            CalculatorButton(symbol = "+", modifier = buttonModifier) { onOperationClick("+") }
        }
        Spacer(modifier = Modifier.height(rowSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(rowSpacing)
        ) {
            CalculatorButton(symbol = "0", modifier = buttonModifier.weight(2f)) { onNumberClick("0") }
            CalculatorButton(symbol = ".", modifier = buttonModifier, onClick = ::onDecimalClick)
            CalculatorButton(symbol = "=", modifier = buttonModifier, onClick = ::onEqualsClick)
        }
    }
}