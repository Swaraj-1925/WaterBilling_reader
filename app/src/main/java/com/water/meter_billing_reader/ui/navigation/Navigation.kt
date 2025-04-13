package com.water.meter_billing_reader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.water.meter_billing_reader.ui.screens.HomeScreen
import com.water.meter_billing_reader.ui.screens.SignInScreen
import com.water.meter_billing_reader.ui.screens.SignUpScreen
import okhttp3.Route

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    startDestination: Any,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable<HomeScreenRoute> {
            HomeScreen(navController = navController)
        }
        composable<SignupScreenRoute> {
            SignUpScreen(navController = navController)
        }
        composable<SigninScreenRoute> {
            SignInScreen(navController = navController)
        }

    }
}