package com.water.meter_billing_reader.di

import com.water.meter_billing_reader.data.services.WaterBillingServerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WaterBillingServerModule {
    @Provides
    @Singleton
    fun provideWaterBillingServer(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://waterbilling-server.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWaterBillingService(retrofit: Retrofit): WaterBillingServerService {
        return retrofit.create(WaterBillingServerService::class.java)
    }
}