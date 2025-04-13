package com.water.meter_billing_reader.ui.screens

import android.R.attr.enabled
import android.R.attr.phoneNumber
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.water.meter_billing_reader.ui.components.ImageUpload
import com.water.meter_billing_reader.ui.navigation.SigninScreenRoute
import com.water.meter_billing_reader.ui.viewmodel.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen( navController: NavController,viewModel: HomeScreenViewModel = hiltViewModel()){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        navController.navigate(SigninScreenRoute)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        HomeScreenBody(paddingValues = paddingValues,viewModel = viewModel)
    }

}
@Composable
fun HomeScreenBody(paddingValues: PaddingValues,viewModel: HomeScreenViewModel){

    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val imageUrl by viewModel.imageUrl.collectAsState()
    val extractedReading by viewModel.extractedReading.collectAsState()
    val showManualInput by viewModel.showManualInput.collectAsState()
    val manualReading by viewModel.manualReading.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(paddingValues = paddingValues)
    ) {
        item {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { viewModel.onPhoneNumberChange(it) },
                label = { Text("Phone Number") },
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            ImageUpload(viewmodel = viewModel)
        }
        item {
            if (imageUrl != null) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Water Meter Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Extracted Reading: $extractedReading")

                    if (showManualInput) {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = manualReading,
                            onValueChange = { viewModel.onManualReadingChange(it) },
                            label = { Text("Enter Correct Reading") }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(onClick = {
                            viewModel.toggleManualInput(true)
                        }) {
                            Text("No")
                        }

                        Button(
                            onClick = { viewModel.calculateBill() },
                            enabled = !extractedReading.isNullOrBlank() || (showManualInput && manualReading.isNotBlank())
                        ) {
                            Text("Yes, send bill")
                        }
                    }

                }
            }
        }
    }
}