package com.calc.matrixcalculator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calc.matrixcalculator.ui.screens.HomeScreen
import com.calc.matrixcalculator.ui.screens.matrix.MatrixScreen
import com.calc.matrixcalculator.ui.screens.vector.VectorScreen

@Composable
fun CalCNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("matrix") {
            MatrixScreen(navController = navController)
        }
        composable("vector") {
            VectorScreen(navController = navController)
        }
    }
}
