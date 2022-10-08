package com.example.assetmanagementapp.data.remote

import com.example.assetmanagementapp.data.remote.api.model.refreshtoken.RefreshTokenRequest
import com.example.assetmanagementapp.data.remote.api.model.refreshtoken.RefreshTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenService {
    @POST("api/iam/refresh_token")
    fun postRefreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Call<RefreshTokenResponse>
}
