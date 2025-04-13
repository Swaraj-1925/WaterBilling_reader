package com.water.meter_billing_reader.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val _prefsName by lazy {
        val prefs = context.getSharedPreferences("app_instance", Context.MODE_PRIVATE)
        var instanceId = prefs.getString("instance_id", null)
        if (instanceId == null) {
            instanceId = UUID.randomUUID().toString()
            prefs.edit().putString("instance_id", instanceId).apply()
        }
        "com.water.meter_billing_reader.PREFERENCES_$instanceId"
    }

    private val _accessToken = "access_token"
    private fun getEncryptedSharedPreferences(): SharedPreferences {
        return try {
            val masterKeyAlias = MasterKey
                .Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            EncryptedSharedPreferences.create(
                context,
                _prefsName,
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        } catch (e: Exception) {
            Log.e("TokenManager", "Error creating encrypted shared preferences", e)
            when (e) {
                is javax.crypto.AEADBadTagException -> {
                    context.getSharedPreferences(_prefsName, Context.MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply()
                    return getEncryptedSharedPreferences()
                }

                else -> throw e
            }
        }

    }
    fun saveAccessToken(accessToken: String) {
        try {
            val editor = getEncryptedSharedPreferences().edit()
            editor.putString(_accessToken, accessToken)
            editor.apply()
            Log.i("TokenManager", "TOKEN SAVED")
        } catch (e: Exception) {
            Log.e("TokenManager", "Error saving access token", e)
        }
    }
    fun getAccessToken(): String? {
        return getEncryptedSharedPreferences().getString(_accessToken, null)
    }

    fun clearToken() {
        val editor = getEncryptedSharedPreferences().edit()
        editor.remove(_accessToken)
        editor.apply()
        Log.i("TokenManager", "All tokens cleared")
    }
}