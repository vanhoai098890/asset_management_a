package com.example.assetmanagementapp.data.remote

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.changepassword.ChangePasswordRequest
import com.example.assetmanagementapp.data.remote.api.model.changepassword.ChangePasswordResponse
import com.example.assetmanagementapp.data.remote.api.model.checkotp.CheckOtpRequestDto
import com.example.assetmanagementapp.data.remote.api.model.checkotp.VerifyOTPRequest
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItem
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItemResponse
import com.example.assetmanagementapp.data.remote.api.model.consignment.ItemConsignmentRequest
import com.example.assetmanagementapp.data.remote.api.model.consignment.SearchListConsignmentRequest
import com.example.assetmanagementapp.data.remote.api.model.customer.CityListResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.CountryListResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerProperty
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerPropertyResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.ListUserInfoResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.MajorListResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.ProfileRequest
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfoResponse
import com.example.assetmanagementapp.data.remote.api.model.department.AddDepartmentItemResponse
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentAdditionRequest
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentDetailRequest
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentItemResponse
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.CheckDeviceExistRequest
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.CheckDeviceExistResponse
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.DetailDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.DetailDeviceResponse
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.EditDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.device.ListDeviceMainResponse
import com.example.assetmanagementapp.data.remote.api.model.device.ListMainDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItemResponse
import com.example.assetmanagementapp.data.remote.api.model.favourite.SaveDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.ForgetPasswordRequestDto
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.ForgetPasswordResponse
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.InputPhoneResponse
import com.example.assetmanagementapp.data.remote.api.model.infomain.InfoMainResponse
import com.example.assetmanagementapp.data.remote.api.model.logout.LogoutRequestDto
import com.example.assetmanagementapp.data.remote.api.model.provider.ProviderItemResponse
import com.example.assetmanagementapp.data.remote.api.model.qrcode.QrcodeRequest
import com.example.assetmanagementapp.data.remote.api.model.qrcode.QrcodeResponse
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.ResetPasswordRequestDto
import com.example.assetmanagementapp.data.remote.api.model.room.AddRoomRequest
import com.example.assetmanagementapp.data.remote.api.model.room.AssetItemRequest
import com.example.assetmanagementapp.data.remote.api.model.room.ListRoomItemResponse
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItemResponse
import com.example.assetmanagementapp.data.remote.api.model.searchdevice.SearchListDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.setnewpassword.ConfirmForgotPasswordRequestDto
import com.example.assetmanagementapp.data.remote.api.model.setpassword.SetPasswordRequestDto
import com.example.assetmanagementapp.data.remote.api.model.setpassword.SetPasswordResponseDto
import com.example.assetmanagementapp.data.remote.api.model.signin.request.SignInRequestDto
import com.example.assetmanagementapp.data.remote.api.model.signin.response.SignInResponseDto
import com.example.assetmanagementapp.data.remote.api.model.signup.request.SignUpRequestDto
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetItemResponse
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetRequest
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @POST("api/admin/customer/signup")
    suspend fun signUp(@Body signUpRequestDto: SignUpRequestDto): Response<CommonResponse>

    @POST("api/admin/customer/edit")
    suspend fun editUser(@Body signUpRequestDto: SignUpRequestDto): Response<CommonResponse>

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
    suspend fun getOtpResendInfo(@Body inputPhoneRequest: InputPhoneRequest): Response<CommonResponse>

    @POST("api/iam/verify-otp")
    suspend fun postVerifyOTP(@Body verifyOTPRequest: VerifyOTPRequest): Response<CommonResponse>

    @POST("api/user_info")
    suspend fun getUserInfo(@Body phoneRequest: InputPhoneRequest): Response<UserInfoResponse>

    @POST("api/device/get_list_favourite")
    suspend fun getFavouriteDevices(
        @Body listMainDeviceRequest: ListMainDeviceRequest
    ): Response<DeviceItemResponse>

    @POST("api/device/save")
    suspend fun saveDevice(@Body saveDeviceRequest: SaveDeviceRequest): Response<CommonResponse>

    @POST("api/device/un_save")
    suspend fun unSaveDevice(@Body saveDeviceRequest: SaveDeviceRequest): Response<CommonResponse>

    @POST("api/device/detail_device")
    suspend fun getDetailDevice(@Body detailDeviceRequest: DetailDeviceRequest): Response<DetailDeviceResponse>

    @POST("api/device/list_main_device")
    suspend fun getListMainDevice(@Body listMainDeviceRequest: ListMainDeviceRequest): Response<ListDeviceMainResponse>

    @POST("api/customer/update_profile")
    suspend fun updateProfiles(@Body profileRequest: ProfileRequest): Response<CommonResponse>

    @POST("api/device/category")
    suspend fun getCategories(@Body typeAssetRequest: TypeAssetRequest): Response<TypeAssetResponse>

    @GET("api/device/category")
    suspend fun getCategories(): Response<TypeAssetResponse>

    @GET("api/device/status_type")
    suspend fun getStatusType(): Response<TypeAssetResponse>

    @GET("api/provider/get_all")
    suspend fun getProviders(): Response<ProviderItemResponse>

    @POST("api/device/search_asset")
    suspend fun searchListDevice(@Body searchListDeviceRequest: SearchListDeviceRequest): Response<DeviceItemResponse>

    @GET("api/device/get_info_main")
    suspend fun getInfoMain(): Response<InfoMainResponse>

    @GET("api/department/get_all_department")
    suspend fun getDepartments(): Response<DepartmentItemResponse>

    @POST("api/admin/department/add_department")
    suspend fun addDepartment(@Body departmentAdditionRequest: DepartmentAdditionRequest): Response<AddDepartmentItemResponse>

    @POST("api/department/get_room_by_department_id")
    suspend fun getRoomsByDepartmentId(@Body detailDepartmentRequest: DepartmentDetailRequest): Response<ListRoomItemResponse>

    @GET("api/department/get_room")
    suspend fun getRooms(): Response<ListRoomItemResponse>

    @POST("api/admin/room/add_room")
    suspend fun addRoomByDepartmentId(@Body addRoomRequest: AddRoomRequest): Response<RoomItemResponse>

    @POST("api/room/get_asset_by_id")
    suspend fun getAssetByRoomId(@Body assetItemRequest: AssetItemRequest): Response<DeviceItemResponse>

    @POST("api/device/generate_qr")
    suspend fun generateQrcode(@Body qrcodeRequest: QrcodeRequest): Response<QrcodeResponse>

    @POST("api/device/check_device")
    suspend fun checkDeviceExist(@Body checkDeviceExistRequest: CheckDeviceExistRequest): Response<CheckDeviceExistResponse>

    @POST("api/customer/change_password")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse>

    @POST("api/admin/consignment/get_consignment")
    suspend fun getConsignment(@Body itemConsignmentRequest: ItemConsignmentRequest): Response<ConsignmentItemResponse>

    @POST("api/admin/consignment/search_consignment")
    suspend fun searchConsignment(@Body searchListConsignmentRequest: SearchListConsignmentRequest): Response<ConsignmentItemResponse>

    @Multipart
    @POST("api/admin/consignment/add_consignment")
    suspend fun addConsignment(
        @Part("request") consignmentItem: ConsignmentItem,
        @Part file: MultipartBody.Part
    ): Response<CommonResponse>

    @Multipart
    @POST("api/admin/consignment/edit_consignment")
    suspend fun editConsignment(
        @Part("request") consignmentItem: ConsignmentItem,
        @Part file: MultipartBody.Part
    ): Response<CommonResponse>

    @POST("api/admin/consignment/edit_consignment_without_image")
    suspend fun editConsignment(
        @Body consignmentItem: ConsignmentItem
    ): Response<CommonResponse>

    @POST("api/admin/device/edit_device")
    suspend fun editDevice(
        @Body editDeviceRequest: EditDeviceRequest
    ): Response<CommonResponse>

    @POST("api/admin/category/edit_category")
    suspend fun editCategory(
        @Body typeAsset: TypeAsset
    ): Response<CommonResponse>

    @POST("api/admin/category/add_category")
    suspend fun addCategory(
        @Body typeAsset: TypeAsset
    ): Response<TypeAssetItemResponse>

    @GET("api/admin/user/get_all_user")
    suspend fun getAllUser(): Response<ListUserInfoResponse>

    @POST("api/admin/user/search_user")
    suspend fun searchUser(@Body searchListDeviceRequest: SearchListDeviceRequest): Response<ListUserInfoResponse>

    @GET("api/admin/user/get_country")
    suspend fun getCountry(): Response<CountryListResponse>

    @GET("api/admin/user/get_major")
    suspend fun getMajor(): Response<MajorListResponse>

    @GET("api/admin/user/get_city")
    suspend fun getCity(): Response<CityListResponse>

}
