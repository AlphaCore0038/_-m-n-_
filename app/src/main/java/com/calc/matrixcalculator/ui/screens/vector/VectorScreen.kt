package com.calc.matrixcalculator.ui.screens.vector

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
import com.calc.matrixcalculator.ui.screens.matrix.formatDouble
import com.calc.matrixcalculator.viewmodel.VectorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VectorScreen(navController: NavController, viewModel: VectorViewModel = viewModel()) {
    var showSecondVector by remember { mutableStateOf(false) }
    var needsSecondVector by remember { mutableStateOf(false) }
    var needsScalar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vector Calculator") },
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
            // Vector A
            Text("Vector v", style = MaterialTheme.typography.titleMedium)
            DimensionSelectorV(
                dimension = viewModel.dimensionA,
                onDimensionChange = { viewModel.setDimensionA(it) },
            )
            VectorInputRow(
                dimension = viewModel.dimensionA,
                data = viewModel.currentInputA,
                onComponentChange = { i, v -> viewModel.updateComponentA(i, v) },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { viewModel.clearVectorA() }) { Text("Clear") }
            }

            HorizontalDivider()

            // Operations
            Text("Operations", style = MaterialTheme.typography.titleMedium)

            // Two-vector operations
            Text("Two-vector operations", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { needsSecondVector = true; showSecondVector = true; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("v + w") }
                Button(onClick = { needsSecondVector = true; showSecondVector = true; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("v − w") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { needsSecondVector = true; showSecondVector = true; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("v · w") }
                Button(onClick = { needsSecondVector = true; showSecondVector = true; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("v × w") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { needsSecondVector = true; showSecondVector = true; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("Angle") }
                Button(onClick = { needsSecondVector = true; showSecondVector = true; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("Projection") }
            }

            // Scalar
            OutlinedButton(onClick = { needsScalar = true; needsSecondVector = false }, modifier = Modifier.fillMaxWidth()) { Text("Scalar × v") }

            // Single vector ops
            Text("Single-vector operations", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { viewModel.performMagnitude() }, modifier = Modifier.weight(1f)) { Text("|v|") }
                OutlinedButton(onClick = { viewModel.performUnitVector() }, modifier = Modifier.weight(1f)) { Text("û") }
            }

            // Classification
            Text("Classification", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { needsSecondVector = true; showSecondVector = true; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("Parallel?") }
                OutlinedButton(onClick = { needsSecondVector = true; showSecondVector = true; needsScalar = false }, modifier = Modifier.weight(1f)) { Text("Perpendicular?") }
            }

            // Scalar input
            if (needsScalar) {
                OutlinedTextField(
                    value = viewModel.scalarValue,
                    onValueChange = { viewModel.updateScalar(it) },
                    label = { Text("Scalar") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Button(
                    onClick = { viewModel.performScalarMultiply(); needsScalar = false },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Scalar × v") }
            }

            // Second Vector input
            if (showSecondVector && needsSecondVector) {
                HorizontalDivider()
                Text("Vector w", style = MaterialTheme.typography.titleMedium)
                DimensionSelectorV(
                    dimension = viewModel.dimensionB,
                    onDimensionChange = { viewModel.setDimensionB(it) },
                )
                VectorInputRow(
                    dimension = viewModel.dimensionB,
                    data = viewModel.currentInputB,
                    onComponentChange = { i, v -> viewModel.updateComponentB(i, v) },
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { viewModel.clearVectorB() }) { Text("Clear") }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { viewModel.performAdd(); showSecondVector = false; needsSecondVector = false }, modifier = Modifier.weight(1f)) { Text("v + w") }
                    Button(onClick = { viewModel.performSubtract(); showSecondVector = false; needsSecondVector = false }, modifier = Modifier.weight(1f)) { Text("v − w") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { viewModel.performDotProduct(); showSecondVector = false; needsSecondVector = false }, modifier = Modifier.weight(1f)) { Text("v · w") }
                    Button(onClick = { viewModel.performCrossProduct(); showSecondVector = false; needsSecondVector = false }, modifier = Modifier.weight(1f)) { Text("v × w") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { viewModel.performAngle(); showSecondVector = false; needsSecondVector = false }, modifier = Modifier.weight(1f)) { Text("Angle") }
                    Button(onClick = { viewModel.performProjection(); showSecondVector = false; needsSecondVector = false }, modifier = Modifier.weight(1f)) { Text("Projection") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { viewModel.performIsParallel(); showSecondVector = false; needsSecondVector = false }, modifier = Modifier.weight(1f)) { Text("Parallel?") }
                    Button(onClick = { viewModel.performIsPerpendicular(); showSecondVector = false; needsSecondVector = false }, modifier = Modifier.weight(1f)) { Text("Perp?") }
                }
            }

            // Error
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

            // Result
            if (viewModel.resultLabel.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(viewModel.resultLabel, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Spacer(modifier = Modifier.height(8.dp))
                        when {
                            viewModel.resultVector != null -> {
                                val v = viewModel.resultVector!!
                                Row {
                                    for (i in 0 until v.dimension) {
                                        Text(
                                            text = formatDouble(v[i]),
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(4.dp)
                                                .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f))
                                                .padding(8.dp),
                                            textAlign = TextAlign.Center,
                                        )
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
                        if (viewModel.resultVector != null) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = { viewModel.useResultAsInputA() }) { Text("Use as v") }
                                OutlinedButton(onClick = { viewModel.useResultAsInputB() }) { Text("Use as w") }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DimensionSelectorV(dimension: Int, onDimensionChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Dimension:")
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = dimension.toString(),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).width(60.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                (1..10).forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.toString()) },
                        onClick = { onDimensionChange(option); expanded = false },
                    )
                }
            }
        }
    }
}

@Composable
fun VectorInputRow(dimension: Int, data: DoubleArray, onComponentChange: (Int, String) -> Unit) {
    val labels = listOf("x", "y", "z") + (4..10).map { it.toString() }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 0 until dimension) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = labels.getOrElse(i) { "$i" },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.width(24.dp),
                )
                OutlinedTextField(
                    value = if (data[i] == 0.0) "" else formatDouble(data[i]),
                    onValueChange = { onComponentChange(i, it) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                )
            }
        }
    }
}
