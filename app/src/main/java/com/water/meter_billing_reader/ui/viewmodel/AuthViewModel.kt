package com.water.meter_billing_reader.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.water.meter_billing_reader.data.WaterBillingServerRepository
import com.water.meter_billing_reader.utils.AuthFormField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: WaterBillingServerRepository
) : ViewModel() {
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
    private val _address = MutableStateFlow<String>("")
    val address = _address.asStateFlow()

    private val _authenticated = MutableStateFlow<Boolean>(false)
    val authenticated = _authenticated.asStateFlow()

    private val _formErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val formErrors = _formErrors.asStateFlow()

    fun onFieldChanged(field: AuthFormField) {
        when (field) {
            is AuthFormField.Name -> _name.value = field.value
            is AuthFormField.Email -> _email.value = field.value
            is AuthFormField.Password -> _password.value = field.value
            is AuthFormField.ConfirmPassword -> _confirmPassword.value = field.value
            is AuthFormField.PhoneNumber -> _phoneNumber.value = field.value
            is AuthFormField.Address -> _address.value = field.value
        }
    }

    private fun validateForm(): Boolean {
        val errors = mutableMapOf<String, String>()

        if (_name.value.isBlank()) {
            Log.w("AuthViewModel :validateForm", "Name cannot be empty")
            errors["name"] = "Name cannot be empty"
        }

        if (_email.value.isBlank()) {
            Log.w("AuthViewModel :validateForm", "Email cannot be empty")
            errors["email"] = "Email cannot be empty"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()) {
            Log.w("AuthViewModel :validateForm", "Invalid email format")
            errors["email"] = "Invalid email format"
        }

        if (_phoneNumber.value.isBlank()) {
            Log.w("AuthViewModel :validateForm", "Phone number cannot be empty")
            errors["phone"] = "Phone number cannot be empty"
        }

        if (_password.value.isBlank()) {
            Log.w("AuthViewModel :validateForm", "Password cannot be empty")
        }

//        if (_confirmPassword.value.isNotBlank()) {
//            Log.w("AuthViewModel :validateForm", "Passwords do not match")
//            errors["password"] = "Password cannot be empty"
//        }else if (_password.value != _confirmPassword.value){
//
//            errors["confirmPassword"] = "Passwords do not match"
//        }

        if (_address.value.isBlank()) {
            Log.w("AuthViewModel :validateForm", "Address cannot be empty")
            errors["address"] = "Address cannot be empty"
        }

        _formErrors.value = errors
        return errors.isEmpty()
    }
    fun onSingUpClicked() {
        Log.d("AuthViewModel :onSignupClicked", "name: ${_name.value}, email: ${_email.value}, phone: ${_phoneNumber.value}, password: ${_password.value}, address: ${_address.value}")
        if (!validateForm()) return
        viewModelScope.launch {
            _authenticated.value=false
            val res = repository.signUp(
                name = _name.value,
                email = _email.value,
                phone = _phoneNumber.value,
                password = _password.value,
                address = _address.value
            )
            if (res ==200){
                _authenticated.value = true
            }
        }
    }
    fun onSingInClicked() {
        val errors = mutableMapOf<String, String>()

        if (_phoneNumber.value.isBlank()) {
            errors["phone"] = "Phone number cannot be empty"
        }

        if (_password.value.isBlank()) {
            errors["password"] = "Password cannot be empty"
        }

        _formErrors.value = errors
        if (errors.isNotEmpty()) return

        viewModelScope.launch {
            _authenticated.value=false
            val res = repository.signIn(
                phone = _phoneNumber.value,
                password = _password.value
            )
            if (res==200){
                _authenticated.value = true
            }

        }
    }

    fun checkAuthentication(): Boolean{
        return repository.isAuthenticated()
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}