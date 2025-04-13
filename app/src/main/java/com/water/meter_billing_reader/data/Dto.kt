package com.water.meter_billing_reader.data

data class ApiResponse<T>(
    val code: Int,
    val status: String,
    val data: T? = null,
    val error: String? = null,
    val message: String? = null
)

data class MeterReaderSignup(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val address: String
)

data class Signin(
    val phone: String,
    val password: String
)

data class TokenResponse(
    val token: String
)

data class MeterReadingResponse(
    val meter_reading: Int,
    val blob_path: String
)

data class Bill(
    val id: Int,
    val phone: String,
    val reader_id: Int,
    val image_url: String,
    val reading_value: Int,
    val reading_date: String,
    val due_date: String,
    val price: Float,
    val modified: Boolean
)
data class CalculateBillRequest(
    val phone: String,
    val reading: Int,
    val image_url: String,
    val modified: Boolean
)