package com.water.meter_billing_reader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.water.meter_billing_reader.data.WaterBillingServerRepository
import com.water.meter_billing_reader.ui.navigation.HomeScreenRoute
import com.water.meter_billing_reader.ui.navigation.Navigation
import com.water.meter_billing_reader.ui.navigation.SignupScreenRoute
import com.water.meter_billing_reader.ui.screens.HomeScreen
import com.water.meter_billing_reader.ui.screens.SignUpScreen
import com.water.meter_billing_reader.ui.theme.Water_meter_billing_readerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: WaterBillingServerRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Water_meter_billing_readerTheme {
                val startDestination = if (repository.isAuthenticated()) {
                    Log.d("MainActivity", "onCreate: ${repository.isAuthenticated()}")
                    HomeScreenRoute
                } else {
                    Log.d("MainActivity", "onCreate: ${repository.isAuthenticated()}")
                    SignupScreenRoute
                }
                Navigation(startDestination = startDestination)
            }
        }
    }
}
