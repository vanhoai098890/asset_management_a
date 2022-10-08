package com.example.assetmanagementapp.data.remote

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.checkotp.CheckOtpRequestDto
import com.example.assetmanagementapp.data.remote.api.model.checkotp.VerifyOTPRequest
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerProperty
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerPropertyResponse
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.ForgetPasswordRequestDto
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.ForgetPasswordResponse
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.InputPhoneResponse
import com.example.assetmanagementapp.data.remote.api.model.logout.LogoutRequestDto
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.ResetPasswordRequestDto
import com.example.assetmanagementapp.data.remote.api.model.setnewpassword.ConfirmForgotPasswordRequestDto
import com.example.assetmanagementapp.data.remote.api.model.setpassword.SetPasswordRequestDto
import com.example.assetmanagementapp.data.remote.api.model.setpassword.SetPasswordResponseDto
import com.example.assetmanagementapp.data.remote.api.model.signin.request.SignInRequestDto
import com.example.assetmanagementapp.data.remote.api.model.signin.response.SignInResponseDto
import com.example.assetmanagementapp.data.remote.api.model.signup.request.SignUpRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("/api/iam/login")
    suspend fun signIn(@Body signInRequestDto: SignInRequestDto): Response<SignInResponseDto>

    @POST("/api/iam/forget_password")
    suspend fun forgetPassword(@Body forgetPasswordDto: ForgetPasswordRequestDto): Response<ForgetPasswordResponse>

    @POST("/api/iam/check_otp")
    suspend fun checkOtp(@Body checkOtpRequestDto: CheckOtpRequestDto): Response<CommonResponse>

    @POST("/api/iam/reset_password")
    suspend fun resetPassword(@Body resetPasswordRequestDto: ResetPasswordRequestDto): Response<CommonResponse>

    @POST("/api/iam/signup")
    suspend fun signUp(@Body signUpRequestDto: SignUpRequestDto): Response<CommonResponse>

    @POST("/api/iam/logout")
    suspend fun logout(@Body logoutRequestDto: LogoutRequestDto): Response<CommonResponse>

    @GET("api/customer/{id}")
    suspend fun getCustomerById(@Path("id") customerId: Int): Response<CustomerPropertyResponse>

    @PUT("api/customer/{id}")
    suspend fun updateCustomerById(
        @Path("id") customerId: Int,
        @Body requestCustomer: CustomerProperty
    ): Response<CommonResponse>

    @POST("/api/iam/confirm-forgot-password")
    suspend fun confirmForgotPassword(
        @Body confirmForgotPasswordRequestDto: ConfirmForgotPasswordRequestDto
    ): Response<CommonResponse>

    @POST("/api/iam/check-not-exist")
    suspend fun checkPhoneNumberResetPassword(@Body phoneRequest: InputPhoneRequest): Response<InputPhoneResponse>

    @POST("/api/iam/set-password")
    suspend fun getSetPasswordInfo(@Body setPasswordRequest: SetPasswordRequestDto): Response<SetPasswordResponseDto>

    @POST("api/iam/customer")
    suspend fun postPhoneSignUp(@Body phoneRequest: InputPhoneRequest): Response<InputPhoneResponse>

    @POST("api/iam/resend-otp")
    suspend fun getOtpResendInfo(inputPhoneRequest: InputPhoneRequest): Response<CommonResponse>

    @POST("api/iam/verify-otp")
    suspend fun postVerifyOTP(@Body verifyOTPRequest: VerifyOTPRequest): Response<CommonResponse>

}
