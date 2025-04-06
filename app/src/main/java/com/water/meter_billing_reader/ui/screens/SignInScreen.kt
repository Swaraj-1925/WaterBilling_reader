package com.water.meter_billing_reader.ui.screens

import android.R.attr.name
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.water.meter_billing_reader.ui.components.InputBox
import com.water.meter_billing_reader.ui.viewmodel.AuthViewModel
import com.water.meter_billing_reader.utils.AuthFormField
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

@Composable
fun SignInScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val password by viewModel.password.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()

    var showAlert by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    Scaffold (
        modifier = Modifier.fillMaxSize()
    ){ paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.8f)

        ) {
            Text(
                text = "Hey there,\n Welcome Back!!",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            InputBox(
                value = phoneNumber.toString(),
                placeholder = "Phone number",
                onValueChange = {
                    AuthFormField.PhoneNumber(it)
                },
                viewModel = viewModel,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone")
                },
                keyboardType = KeyboardType.Phone
            )
            InputBox(
                value = password,
                placeholder = "Password",
                onValueChange = { AuthFormField.Password(it) },
                viewModel = viewModel,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Password")
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    val phoneDigits = phoneNumber.toString().filter { it.isDigit() }.length
                    if (phoneDigits != 10) {
                        showAlert = true // Show alert if phone number is not exactly 10 digits
                    } else {
                        Log.d("SignUpScreen", "Registration submitted: $password, $phoneNumber")
                        viewModel.onSubmitClicked()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(text = "Register", fontSize = 16.sp)
            }
            if (showAlert) {
                AlertDialog(
                    onDismissRequest = { showAlert = false },
                    title = { Text("Invalid Phone Number") },
                    text = { Text("Please enter a 10-digit phone number.") },
                    confirmButton = {
                        TextButton(onClick = { showAlert = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}
