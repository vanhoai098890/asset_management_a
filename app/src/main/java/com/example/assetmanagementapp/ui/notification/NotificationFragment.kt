package com.example.assetmanagementapp.ui.notification

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_common.utils.LogUtils
import com.example.assetmanagementapp.MainViewModel
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentNotificationBinding
import com.example.assetmanagementapp.ui.editexceldialog.EditExcelDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Date

@AndroidEntryPoint
class NotificationFragment : BaseFragment() {
    private lateinit var binding: FragmentNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val editExcelDialog: EditExcelDialog by lazy {
        EditExcelDialog()
    }
    private val notificationAdapter: NotificationAdapter by lazy {
        NotificationAdapter().apply {
            onDownloadClick = { viewModel.downloadExcelFile(it) }
            onEditClick = { item ->
                editExcelDialog.apply {
                    submitOnClick = { file ->
                        viewModel.updateFileExcelTicket(notification = item, excelFile = file)
                    }
                }.show(parentFragmentManager, null)
            }
            isAdmin = viewModel.stateIsAdmin.value
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        initData()
        initView()
        initObserver()
        return binding.root
    }

    private fun initData() {

    }

    private fun initView() {
        binding.apply {
            rvNotification.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = notificationAdapter
            }
        }
    }

    private fun initObserver() {
        viewModel.store.apply {
            observe(
                owner = this@NotificationFragment,
                selector = { state -> state.stateListNotification },
                observer = {
                    if (it.isNotEmpty()) {
                        notificationAdapter.submitList(it)
                    }
                })
            observe(
                owner = this@NotificationFragment,
                selector = { state -> state.fileResponse },
                observer = { stateBody ->
                    saveFile(
                        body = stateBody,
                        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/" + viewModel.currentState.mapResponse[stateBody] + Date().time + ".xlsx"
                    )
                })
        }
        mainViewModel.store.apply {
            observe(
                owner = this@NotificationFragment,
                selector = { state -> state.stateCLickedNotification },
                observer = {
                    if (it) {
                        viewModel.getNotification()
                        mainViewModel.dispatchClickNotification(false)
                    }
                })
        }
    }

    private val snackBar: Snackbar by lazy {
        Snackbar
            .make(binding.parentLayout, "Download successfully!!", Snackbar.LENGTH_LONG)
            .setAction("View") {
                val intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
                startActivity(intent)

            }
    }

    private fun saveFile(body: ResponseBody?, path: String): String {
        if (body == null)
            return ""
        var input: InputStream? = null
        try {
            input = body.byteStream()
            val fos = FileOutputStream(path)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            LogUtils.d("saveFile $path")
            snackBar.show()
            return path
        } catch (e: Exception) {
            LogUtils.d("saveFile $e")
        } finally {
            input?.close()
        }
        return ""
    }

}
