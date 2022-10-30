package com.example.assetmanagementapp.data.remote.api.model.request

import android.os.Parcelable
import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ReceivedRequestItemResponse(
    @SerializedName("data")
    val data: List<ReceivedRequestItem>
) : CommonResponse()

@Parcelize
data class ReceivedRequestItem(
    val createDate: String = "22/06/2022",
    val image: String = "https://cdn2.cellphones.com.vn/358x/media/catalog/product/1/_/1_221_2.png",
    val nameDevice: String = "Asus VivoBook 14 X413JA",
    val category: String = "Laptop",
    val status: String = "Approved",
    val startDate: String = "23/06/2022",
    val endDate: String = "25/06/2022",
    val requestId: Int = 55
) : Parcelable
