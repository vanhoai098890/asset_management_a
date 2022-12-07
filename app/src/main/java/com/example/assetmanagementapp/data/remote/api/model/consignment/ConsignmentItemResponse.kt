package com.example.assetmanagementapp.data.remote.api.model.consignment

import android.net.Uri
import android.os.Parcelable
import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ConsignmentItemResponse(
    @SerializedName("data")
    val data: List<ConsignmentItem>
) : CommonResponse()

@Parcelize
data class ConsignmentItem(
    @SerializedName("assetName")
    val name: String = "Asus VivoBook 14 X413JA",
    @SerializedName("brand")
    val brand: String = "Asus",
    @SerializedName("consignmentId")
    val consignmentId: Int = 0,
    @SerializedName("consignmentName")
    val consignmentName: String = "Lo0123",
    @SerializedName("unitPrice")
    val unitPrice: Double = 15000.0,
    @SerializedName("numberOfUses")
    val numberOfUses: Int = 0,
    @SerializedName("typeAssetName")
    val typeAssetName: String = "Laptop",
    @SerializedName("typeAssetId")
    val typeAssetId: Int = 0,
    @SerializedName("image")
    val image: String = "https://cdn2.cellphones.com.vn/358x/media/catalog/product/1/_/1_221_2.png",
    @SerializedName("avatarId")
    val avatarId: Int = 0,
    @SerializedName("providerId")
    val providerId: Int = 0,
    @SerializedName("providerName")
    val providerName: String = "Lê Trọng Kha",
    @SerializedName("providerPhone")
    val providerPhone: String = "0935253225",
    @SerializedName("providerEmail")
    val providerEmail: String = "khangu2501@gmail.com",
    @SerializedName("description")
    val description: String = "A laptop, laptop computer, or notebook computer is a small, portable personal computer (PC) with a screen and alphanumeric keyboard. Laptops typically have a clam shell form factor with the screen mounted on the inside of the upper lid and the keyboard on the inside of the lower lid, although 2-in-1 PCs with a detachable keyboard are often marketed as laptops or as having a \"laptop mode\".[1][2] Laptops are folded shut for transportation, and thus are suitable for mobile use.[3] They are so named because they can be practically placed on a person's lap when being used. Today, laptops are used in a variety of settings, such as at work, in education, for playing games, web browsing, for personal multimedia, and for general home computer use.",
    @SerializedName("dateWarranty")
    val dateWarranty: String = "01-12-2025",
    @SerializedName("dateManufacture")
    val dateManufacture: String = "01-12-2022",
    @SerializedName("dateIn")
    val dateIn: String = "01-12-2022",
    @SerializedName("number")
    val number: Int = 0,
    val uriImage: Uri? = null
) : Parcelable
