package com.example.assetmanagementapp.ui.qrcode

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScanQRCodeResult(val data: String) : Parcelable
