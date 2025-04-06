package com.water.meter_billing_reader.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.water.meter_billing_reader.utils.AuthFormField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private val _name = MutableStateFlow<String>("")
    val name = _name.asStateFlow()
    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()
    private val _password = MutableStateFlow<String>("")
    val password = _password.asStateFlow()
    private val _confirmPassword = MutableStateFlow<String>("")
    val confirmPassword = _confirmPassword.asStateFlow()
    private val _phoneNumber = MutableStateFlow<String>("")
    val phoneNumber = _phoneNumber.asStateFlow()

    fun onFieldChanged(field: AuthFormField) {
        when (field) {
            is AuthFormField.Name -> _name.value = field.value
            is AuthFormField.Email -> _email.value = field.value
            is AuthFormField.Password -> _password.value = field.value
            is AuthFormField.ConfirmPassword -> _confirmPassword.value = field.value
            is AuthFormField.PhoneNumber -> _phoneNumber.value = field.value
        }
    }
    fun onSubmitClicked() {

    }
}