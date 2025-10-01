package com.cornellappdev.transit.networking

import android.util.Log
import com.cornellappdev.transit.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RoutesRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EateryRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            Log.d(
                "NetworkRequest",
                message
            )
        }
        logging.level = (HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient
            .Builder()
            .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
            .protocols(listOf(Protocol.HTTP_1_1))
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(LatLngAdapter())
        .build()

    @Provides
    @RoutesRetrofit
    fun provideRoutesRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BACKEND_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @EateryRetrofit
    fun provideEateryRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.EATERY_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    fun provideRoutesNetworkApi(@RoutesRetrofit retrofit: Retrofit): RoutesNetworkApi =
        retrofit.create(RoutesNetworkApi::class.java)

    @Provides
    fun provideEcosystemNetworkApi(@RoutesRetrofit retrofit: Retrofit): EcosystemNetworkApi =
        retrofit.create(EcosystemNetworkApi::class.java)

    @Provides
    fun provideEateryNetworkApi(@EateryRetrofit retrofit: Retrofit): EateryNetworkApi =
        retrofit.create(EateryNetworkApi::class.java)
}