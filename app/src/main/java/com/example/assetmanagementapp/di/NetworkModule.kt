package com.example.assetmanagementapp.di

import com.example.app_common.constant.AppConstant.KEY_AUTHORIZATION
import com.example.app_common.constant.AppConstant.VALUE_AUTHORIZATION_PREFIX
import com.example.app_common.utils.eventbus.EventBus
import com.example.app_common.utils.eventbus.event_model.EventTimeOutApi
import com.example.assetmanagementapp.BuildConfig
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.RefreshTokenAuthenticator
import com.example.assetmanagementapp.data.remote.RefreshTokenService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    companion object {
        const val KEY_CONTENT_TYPE = "Content-Type"
        const val VALUE_CONTENT_TYPE = "application/json"
        const val NETWORK_CONNECT_TIMEOUT = 30
        const val NETWORK_WRITE_TIMEOUT = 30
        const val NETWORK_READ_TIMEOUT = 30
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        loginSessionManager: LoginSessionManager,
        refreshTokenAuthenticator: RefreshTokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder().also { builder ->
            builder.addInterceptor(requestInterceptor(loginSessionManager))
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(httpLoggingInterceptor())
            }
            builder.authenticator(refreshTokenAuthenticator)
            builder.protocols(Collections.singletonList(Protocol.HTTP_1_1))
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRefreshTokenService(): RefreshTokenService {
        return Retrofit.Builder()
            .client(httpClientForRefreshToken())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build().create(RefreshTokenService::class.java)
    }

    private fun httpClientForRefreshToken() = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(httpLoggingInterceptor())
        }
        addInterceptor(Interceptor { chain ->
            chain.request()
            return@Interceptor chain.withConnectTimeout(NETWORK_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .withWriteTimeout(NETWORK_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .withReadTimeout(NETWORK_READ_TIMEOUT, TimeUnit.SECONDS)
                .proceed(chain.request().newBuilder().build())
        })
    }.build()

    private fun httpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun requestInterceptor(loginSessionManager: LoginSessionManager): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .method(original.method, original.body)
            requestBuilder.addHeader(KEY_CONTENT_TYPE, VALUE_CONTENT_TYPE)

            loginSessionManager.getTokenAuthorizationRequest()?.let {
                if (it.isNotBlank()) {
                    requestBuilder.addHeader(
                        name = KEY_AUTHORIZATION,
                        value = "$VALUE_AUTHORIZATION_PREFIX $it"
                    )
                }
            }
            val request = requestBuilder.build()
            try {
                return@Interceptor chain.withConnectTimeout(
                    NETWORK_CONNECT_TIMEOUT,
                    TimeUnit.SECONDS
                )
                    .withWriteTimeout(NETWORK_WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .withReadTimeout(NETWORK_READ_TIMEOUT, TimeUnit.SECONDS)
                    .proceed(request)
            } catch (e: SocketTimeoutException) {
                GlobalScope.launch {
                    EventBus.invokeEvent(EventTimeOutApi())
                }
                throw SocketTimeoutException()
            }
        }
    }
}
