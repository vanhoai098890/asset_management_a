package com.example.assetmanagementapp.utils

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.example.app_common.constant.AppConstant.EMAIL_INFO
import com.example.app_common.constant.AppConstant.MAIL_TO
import com.example.app_common.utils.LogUtils
import com.example.assetmanagementapp.R


object Utils {
    fun callHotLine(context: Context?, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${phone}")
        context?.startActivity(intent)
    }

    fun sendEmail(context: Context?, customerId: String) {
        try {
            context?.let {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse(MAIL_TO)
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(EMAIL_INFO))
                intent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    context.getString(R.string.contact_support_mail_subject, customerId)
                )
                intent.putExtra(
                    Intent.EXTRA_TEXT, ""
                )
                context.startActivity(intent)
            }
        } catch (ex: ActivityNotFoundException) {
            LogUtils.e(ex.message ?: "")
        }
    }

    @SuppressLint("Recycle")
    fun getPath(context: Context, uri: Uri): String? {
        var tempUri = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (DocumentsContract.isDocumentUri(context.applicationContext, tempUri)) {
            if (isExternalStorageDocument(tempUri)) {
                val docId = DocumentsContract.getDocumentId(tempUri)
                val split = docId.split(":".toRegex()).toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(tempUri)) {
                val id = DocumentsContract.getDocumentId(tempUri)
                tempUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(tempUri)) {
                val docId = DocumentsContract.getDocumentId(tempUri)
                val split = docId.split(":".toRegex()).toTypedArray()
                when (split[0]) {
                    "image" -> {
                        tempUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        tempUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        tempUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(tempUri.scheme, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor?
            try {
                cursor =
                    context.contentResolver.query(tempUri, projection, selection, selectionArgs, null)
                val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex)
                }
            } catch (e: Exception) {
            }
        } else if ("file".equals(tempUri.scheme, ignoreCase = true)) {
            return tempUri.path
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}
