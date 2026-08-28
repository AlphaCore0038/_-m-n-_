package com.calc.matrixcalculator.ui.screens.matrix

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.calc.matrixcalculator.viewmodel.MatrixViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatrixScreen(navController: NavController, viewModel: MatrixViewModel = viewModel()) {
    var showSecondMatrix by remember { mutableStateOf(false) }
    var needsSecondMatrix by remember { mutableStateOf(false) }
    var needsScalar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Matrix Calculator") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Matrix A dimensions
            Text("Matrix A", style = MaterialTheme.typography.titleMedium)
            DimensionSelector(
                rows = viewModel.rowsA,
                cols = viewModel.colsA,
                onRowsChange = { viewModel.setDimensions(it, viewModel.colsA) },
                onColsChange = { viewModel.setDimensions(viewModel.rowsA, it) },
            )

            // Matrix A input
            MatrixInputGrid(
                rows = viewModel.rowsA,
                cols = viewModel.colsA,
                data = viewModel.currentInput,
                onCellChange = { i, j, v -> viewModel.updateCell(i, j, v) },
            )

            // Quick actions for A
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { viewModel.setZeroMatrix() }) { Text("Zero") }
                if (viewModel.rowsA == viewModel.colsA) {
                    OutlinedButton(onClick = { viewModel.setIdentityMatrix() }) { Text("Identity") }
                }
                OutlinedButton(onClick = { viewModel.clearMatrix() }) { Text("Clear") }
            }

            HorizontalDivider()

            // Operations section
            Text("Operations", style = MaterialTheme.typography.titleMedium)

            // Binary matrix operations
            Text("Two-matrix operations", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { needsSecondMatrix = true; showSecondMatrix = true; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("A + B") }
                Button(onClick = { needsSecondMatrix = true; showSecondMatrix = true; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("A − B") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { needsSecondMatrix = true; showSecondMatrix = true; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("A × B") }
                OutlinedButton(onClick = { needsScalar = true; needsSecondMatrix = false }, modifier = Modifier.weight(1f)) { Text("Scalar × A") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { needsScalar = true; needsSecondMatrix = false }, modifier = Modifier.weight(1f)) { Text("A^n") }
                OutlinedButton(onClick = { viewModel.performTranspose(); needsSecondMatrix = false; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("A^T") }
            }

            // Scalar input (when needed)
            if (needsScalar) {
                OutlinedTextField(
                    value = viewModel.scalarValue,
                    onValueChange = { viewModel.updateScalar(it) },
                    label = { Text("Scalar / Power") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { viewModel.performScalarMultiply(); needsScalar = false },
                        modifier = Modifier.weight(1f),
                    ) { Text("Scalar × A") }
                    Button(
                        onClick = { viewModel.performPower(); needsScalar = false },
                        modifier = Modifier.weight(1f),
                    ) { Text("A^n") }
                }
            }

            HorizontalDivider()

            // Single matrix operations
            Text("Single-matrix operations", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { viewModel.performDeterminant() }, modifier = Modifier.weight(1f)) { Text("det(A)") }
                OutlinedButton(onClick = { viewModel.performInverse() }, modifier = Modifier.weight(1f)) { Text("A⁻¹") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { viewModel.performRank() }, modifier = Modifier.weight(1f)) { Text("rank(A)") }
                OutlinedButton(onClick = { viewModel.performTrace() }, modifier = Modifier.weight(1f)) { Text("tr(A)") }
            }

            HorizontalDivider()

            // Elimination
            Text("Elimination", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { viewModel.performREF() }, modifier = Modifier.weight(1f)) { Text("REF") }
                OutlinedButton(onClick = { viewModel.performRREF() }, modifier = Modifier.weight(1f)) { Text("RREF") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { viewModel.performGaussianElimination() }, modifier = Modifier.weight(1f)) { Text("Gaussian") }
                OutlinedButton(onClick = { viewModel.performGaussJordanElimination() }, modifier = Modifier.weight(1f)) { Text("Gauss-Jordan") }
            }

            HorizontalDivider()

            // Classification
            Text("Classification", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { viewModel.performIsSymmetric() }, modifier = Modifier.weight(1f)) { Text("Symmetric?") }
                OutlinedButton(onClick = { viewModel.performIsSkewSymmetric() }, modifier = Modifier.weight(1f)) { Text("Skew-sym?") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { viewModel.performIsOrthogonal() }, modifier = Modifier.weight(1f)) { Text("Orthogonal?") }
                OutlinedButton(onClick = { viewModel.performIsSingular() }, modifier = Modifier.weight(1f)) { Text("Singular?") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { viewModel.performIsPositiveDefinite() }, modifier = Modifier.weight(1f)) { Text("Pos Def?") }
                OutlinedButton(onClick = { viewModel.performIsNegativeDefinite() }, modifier = Modifier.weight(1f)) { Text("Neg Def?") }
            }

            // Second Matrix B input
            if (showSecondMatrix && needsSecondMatrix) {
                HorizontalDivider()
                Text("Matrix B", style = MaterialTheme.typography.titleMedium)
                DimensionSelector(
                    rows = viewModel.rowsB,
                    cols = viewModel.colsB,
                    onRowsChange = { viewModel.setDimensionsB(it, viewModel.colsB) },
                    onColsChange = { viewModel.setDimensionsB(viewModel.rowsB, it) },
                )
                MatrixInputGrid(
                    rows = viewModel.rowsB,
                    cols = viewModel.colsB,
                    data = viewModel.currentInputB ?: Array(viewModel.rowsB) { r -> DoubleArray(viewModel.colsB) { c -> viewModel.matrixB[r, c] } },
                    onCellChange = { i, j, v -> viewModel.updateCellB(i, j, v) },
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { viewModel.setZeroMatrixB() }) { Text("Zero") }
                    if (viewModel.rowsB == viewModel.colsB) {
                        OutlinedButton(onClick = { viewModel.setIdentityMatrixB() }) { Text("Identity") }
                    }
                    OutlinedButton(onClick = { viewModel.clearMatrixB() }) { Text("Clear") }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { viewModel.performAdd(); showSecondMatrix = false; needsSecondMatrix = false },
                        modifier = Modifier.weight(1f),
                    ) { Text("A + B") }
                    Button(
                        onClick = { viewModel.performSubtract(); showSecondMatrix = false; needsSecondMatrix = false },
                        modifier = Modifier.weight(1f),
                    ) { Text("A − B") }
                }
                Button(
                    onClick = { viewModel.performMultiply(); showSecondMatrix = false; needsSecondMatrix = false },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("A × B") }
            }

            // Error display
            viewModel.errorMessage?.let { msg ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }

            // Result display
            viewModel.resultLabel.let { label ->
                if (label.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(label, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Spacer(modifier = Modifier.height(8.dp))
                            when {
                                viewModel.resultMatrix != null -> {
                                    val m = viewModel.resultMatrix!!
                                    for (i in 0 until m.rows) {
                                        Row {
                                            for (j in 0 until m.cols) {
                                                Text(
                                                    text = formatDouble(m[i, j]),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .padding(4.dp)
                                                        .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f))
                                                        .padding(8.dp),
                                                    textAlign = TextAlign.Center,
                                                    style = MaterialTheme.typography.bodySmall,
                                                )
                                            }
                                        }
                                    }
                                }
                                viewModel.resultScalar != null -> {
                                    Text(
                                        text = formatDouble(viewModel.resultScalar!!),
                                        style = MaterialTheme.typography.headlineMedium,
                                    )
                                }
                                viewModel.resultBoolean != null -> {
                                    Text(
                                        text = if (viewModel.resultBoolean!!) "Yes" else "No",
                                        style = MaterialTheme.typography.headlineMedium,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            if (viewModel.resultMatrix != null) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(onClick = { viewModel.useResultAsInputA() }) { Text("Use as A") }
                                    OutlinedButton(onClick = { viewModel.useResultAsInputB() }) { Text("Use as B") }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun DimensionSelector(rows: Int, cols: Int, onRowsChange: (Int) -> Unit, onColsChange: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Rows:")
        DropdownSelector(value = rows, range = 1..10, onValueChange = onRowsChange)
        Text("Cols:")
        DropdownSelector(value = cols, range = 1..10, onValueChange = onColsChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(value: Int, range: IntRange, onValueChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = value.toString(),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).width(60.dp),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            range.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = { onValueChange(option); expanded = false },
                )
            }
        }
    }
}

@Composable
fun MatrixInputGrid(rows: Int, cols: Int, data: Array<DoubleArray>, onCellChange: (Int, Int, String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 0 until rows) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                for (j in 0 until cols) {
                    OutlinedTextField(
                        value = if (data[i][j] == 0.0) "" else formatDouble(data[i][j]),
                        onValueChange = { onCellChange(i, j, it) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center),
                    )
                }
            }
        }
    }
}

fun formatDouble(value: Double): String {
    return if (value == value.toLong().toDouble()) {
        value.toLong().toString()
    } else {
        val formatted = "%.6f".format(value).trimEnd('0').trimEnd('.')
        formatted
    }
}
