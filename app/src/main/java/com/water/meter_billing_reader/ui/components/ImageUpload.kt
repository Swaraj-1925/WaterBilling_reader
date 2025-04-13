package com.water.meter_billing_reader.ui.components

import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImage.CancelledResult.uriContent
import com.canhub.cropper.CropImageContract
import com.water.meter_billing_reader.ui.viewmodel.HomeScreenViewModel

@Composable
fun ImageUpload(
    viewmodel: HomeScreenViewModel,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val cropOption = CropImageContractOptions(
        uri = uriContent,
        cropImageOptions = CropImageOptions(
            imageSourceIncludeCamera = true,
            imageSourceIncludeGallery = false,
        )
    )

    val cropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
            Log.d("HomeScreen", "Successfully received uriContent: ${result.uriContent}")
            imageUri?.let { uri ->
//                val source = ImageDecoder.createSource(context.contentResolver, uri)
//                val bitmap = ImageDecoder.decodeBitmap(source)
//                viewmodel.processImage(bitmap)
                viewmodel.getMeterReading(context = context, uri = uri)
            }
        } else {
            val exception = result.error
            Log.e("HomeScreen", "Error receiving bitmap",exception)
            Toast.makeText(context, "Something went wrong, please try again!!", Toast.LENGTH_SHORT).show()
        }
    }
    Button(
        onClick = { cropLauncher.launch(input = cropOption) },
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Launch Image Cropper")
    }
    Text(
        text = "Make sure only reading part is visible in the image",
        fontSize = MaterialTheme.typography.bodySmall.fontSize,
        modifier = Modifier.padding(top = 8.dp)
    )

}