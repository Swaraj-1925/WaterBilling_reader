package com.water.meter_billing_reader.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.net.Uri
import com.water.meter_billing_reader.data.Bill
import com.water.meter_billing_reader.data.WaterBillingServerRepository

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: WaterBillingServerRepository
) : ViewModel() {
    private val _phoneNumber = MutableStateFlow<String>("")
    val phoneNumber = _phoneNumber.asStateFlow()
    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl = _imageUrl.asStateFlow()
    private val _extractedReading = MutableStateFlow<String?>(null)
    val extractedReading = _extractedReading.asStateFlow()

    private val _showManualInput = MutableStateFlow(false)
    val showManualInput = _showManualInput.asStateFlow()

    private val _manualReading = MutableStateFlow("")
    val manualReading = _manualReading.asStateFlow()

    private val _bill = MutableStateFlow<Bill?>(null)
    val bill = _bill.asStateFlow()


    fun onPhoneNumberChange(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }
    fun toggleManualInput(show: Boolean) {
        _showManualInput.value = show
    }

    fun onManualReadingChange(reading: String) {
        _manualReading.value = reading
    }

    fun getMeterReading(context: Context,uri: Uri){
        viewModelScope.launch {
            val response = repository.getMeterReading(uri = uri, phone = phoneNumber.value, context = context)
            Log.d("HomeScreenViewModel : getMeterReading", "Response: $response")

            response?.let {
                Log.d("getMeterReading", "Setting reading to: ${it.meter_reading}")
                _extractedReading.value = it.meter_reading.toString()
                _imageUrl.value = it.blob_path
            }
        }

    }
    fun calculateBill(){
        viewModelScope.launch {
            val modified = extractedReading.value != manualReading.value

            Log.d("calculateBill", "Reading values - extracted: '${extractedReading.value}', manual: '${manualReading.value}'")
//            val readingStr = if (modified) manualReading.value else extractedReading.value.orEmpty()
            val readingStr = if (showManualInput.value) manualReading.value else extractedReading.value.orEmpty()
            if (readingStr.isBlank()) {
                Log.e("HomeScreenViewModel", "Reading is empty. Cannot calculate bill.")
                return@launch
            }

            val reading = readingStr.toIntOrNull()
            if (reading == null) {
                Log.e("HomeScreenViewModel", "Invalid reading: '$readingStr'")
                return@launch
            }
            val response = repository.calculateBill(
                phone = phoneNumber.value,
                reading = reading,
                imageUrl = imageUrl.value.toString()    ,
                modified = showManualInput.value
            )
            response?.let {
                _bill.value = it
                Log.d("HomeScreenViewModel : calculateBill", "Response: $it")
            }
        }

    }
    fun logout(){
        viewModelScope.launch {
            repository.logout()
            Log.d("HomeScreenViewModel", ":logout -m Logged out")
        }
    }
}