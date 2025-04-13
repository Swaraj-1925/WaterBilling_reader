package com.water.meter_billing_reader.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.water.meter_billing_reader.data.services.WaterBillingServerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class WaterBillingServerRepository @Inject constructor(
    private val service: WaterBillingServerService,
    private val tokenManager: TokenManager
){
    suspend fun signUp(
        name: String,
        email: String,
        phone: String,
        password: String,
        address: String
    ): Int{
        try {
            Log.d("WaterBillingServerRepository : SignUp", "name: $name, email: $email, phone: $phone, password: $password, address: $address")
            val signupData = MeterReaderSignup(
                name = name,
                email = email,
                phone = phone,
                password = password,
                address = address
            )
            val response = service.signup(signupData)
//            Log.d("WaterBillingServerRepository : SignUp", "Response: $response")
            val token = response.data?.token
            if (token != null){
                tokenManager.saveAccessToken(token)
                Log.d("WaterBillingServerRepository : SignUp", "Token Saved $token")

                return 200
            }else{
                Log.e("WaterBillingServerRepository : SignUp", "Token is null")
                return 500
            }
        }catch (e: Exception) {
            Log.e("WaterBillingServerRepository : SignUp", "Error: ${e.message}")
            return 404
        }
    }
    suspend fun signIn(phone: String, password: String): Int{
        try {
            val signinData = Signin(
                phone = phone,
                password = password
            )

            val response =  service.login(signinData)
            Log.d("WaterBillingServerRepository : SignIn", "Response: $response")
            return 200
        }catch (e: Exception) {
            Log.e("WaterBillingServerRepository : SignIn", "Error: ${e.message}")
            return 404
        }
    }
    fun isAuthenticated(): Boolean{
        Log.d("WaterBillingServerRepository : isAuthenticated", "Token: ${tokenManager.getAccessToken()}")
        return tokenManager.getAccessToken() != null
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            tokenManager.clearToken()
        }
    }
    suspend fun getMeterReading(context: Context,phone: String,uri: Uri): MeterReadingResponse? {
        try {

//            val token = "${tokenManager.getAccessToken()}"

            val token = "Bearer ${tokenManager.getAccessToken()}"
            val inputStream = context.contentResolver.openInputStream(uri)
            val requestBody =
                inputStream!!.readBytes().toRequestBody("image/png".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", "meter.png", requestBody)

            val response = service.takeMeterReading(
                token = token,
                phone = phone,
//            reading = 0,
//            modified = false,
                image = imagePart
            )
            Log.d("WaterBillingServerRepository : getMeterReading", "Response: $response")
            return response.data
        }catch (e: Exception){
            Log.e("WaterBillingServerRepository : getMeterReading", "Error: ${e.message}")
            return null
        }
    }
    suspend fun calculateBill(
        phone: String,
        reading: Int,
        imageUrl: String,
        modified: Boolean
    ): Bill?{
        try {
            val token = "Bearer ${tokenManager.getAccessToken()}"
            val billData = CalculateBillRequest(
                phone = phone,
                reading = reading,
                image_url = imageUrl,
                modified = modified
            )
            val response = service.calculateBill(
                token = token,
                billData = billData
            )
            return response.data
        }catch (e: Exception){
            Log.e("WaterBillingServerRepository : calculateBill", "Error: ${e.message}")
            return null
        }

    }
}

