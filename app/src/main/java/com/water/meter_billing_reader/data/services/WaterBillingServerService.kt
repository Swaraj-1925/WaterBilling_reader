package com.water.meter_billing_reader.data.services

import com.water.meter_billing_reader.data.ApiResponse
import com.water.meter_billing_reader.data.Bill
import com.water.meter_billing_reader.data.CalculateBillRequest
import com.water.meter_billing_reader.data.MeterReaderSignup
import com.water.meter_billing_reader.data.MeterReadingResponse
import com.water.meter_billing_reader.data.Signin
import com.water.meter_billing_reader.data.TokenResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface WaterBillingServerService {

    @POST("meter/signup")
    suspend fun signup(
        @Body signupData: MeterReaderSignup
    ): ApiResponse<TokenResponse>

    @POST("meter/login")
    suspend fun login(
        @Body signinData: Signin
    ): ApiResponse<TokenResponse>

    @Multipart
    @POST("meter/take_meter_reading")
    suspend fun takeMeterReading(
        @Header("Authorization") token: String,
        @Part("phone") phone: String,
//        @Part("reading") reading: Int,
//        @Part("modified") modified: Boolean,
        @Part image: MultipartBody.Part
    ): ApiResponse<MeterReadingResponse>

    @POST("meter/calculate_bill")
    suspend fun calculateBill(
        @Header("Authorization") token: String,
        @Body billData: CalculateBillRequest
    ): ApiResponse<Bill>
}