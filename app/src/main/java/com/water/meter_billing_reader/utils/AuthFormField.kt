package com.water.meter_billing_reader.utils

sealed class AuthFormField {
    data class Name(val value: String) : AuthFormField()
    data class Email(val value: String) : AuthFormField()
    data class Password(val value: String) : AuthFormField()
    data class Address(val value: String): AuthFormField()
    data class ConfirmPassword(val value: String) : AuthFormField()
    data class PhoneNumber(val value: String) : AuthFormField()
}