package com.water.meter_billing_reader.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.water.meter_billing_reader.ui.viewmodel.AuthViewModel
import com.water.meter_billing_reader.utils.AuthFormField

@Composable
fun InputBox(
    viewModel: AuthViewModel,
    placeholder: String,
    value: String,
    onValueChange: (String) -> AuthFormField,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null, // Optional leading icon
    keyboardType: KeyboardType = KeyboardType.Text, // Default keyboard type
    visualTransformation: VisualTransformation = VisualTransformation.None // Default visual transformation

){
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            viewModel.onFieldChanged(onValueChange(newValue))
        },
        placeholder = { Text(placeholder) },
        leadingIcon = leadingIcon, // Add the icon here
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(25.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType), // Support different keyboard types
        visualTransformation = visualTransformation, // Support password masking
        trailingIcon = trailingIcon,
        singleLine = true
    )
}