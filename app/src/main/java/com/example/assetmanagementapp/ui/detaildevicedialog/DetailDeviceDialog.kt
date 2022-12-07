package com.example.assetmanagementapp.ui.detaildevicedialog

import android.app.Dialog
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.base.BaseDialogFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItem
import com.example.assetmanagementapp.ui.roombottomsheet.RoomBottomSheet
import com.example.assetmanagementapp.ui.statusbottomsheet.StatusBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailDeviceDialog : BaseDialogFragment() {
    private lateinit var btnClose: ImageView
    private lateinit var edtRoom: EditText
    private lateinit var edtStatus: EditText
    private lateinit var btnSubmit: Button
    var roomName: String = ""
    var statusName: String = ""
    var deviceId: Int = 0
    var handleBackPress: (String, String) -> Unit = { _, _ -> }
    private val viewModel: DetailDeviceDialogViewModel by viewModels()
    private val roomBottomSheet: RoomBottomSheet by lazy {
        RoomBottomSheet().apply {
            roomOnClick = {
                viewModel.dispatchStateRoom(it)
            }
        }
    }
    private val statusBottomSheet: StatusBottomSheet by lazy {
        StatusBottomSheet().apply {
            statusOnClick = {
                viewModel.dispatchStateStatus(it.typeName)
            }
        }
    }

    override fun setContentDialog(dialog: Dialog) {
        dialog.apply {
            setContentView(R.layout.dialog_edit_device)
            btnClose = findViewById(R.id.ivClose)
            btnSubmit = findViewById(R.id.btnSubmit)
            edtRoom = findViewById(R.id.edtRoom)
            edtStatus = findViewById(R.id.edtStatus)
            viewModel.dispatchStateRoom(RoomItem(roomId = 0, roomName = roomName))
            viewModel.dispatchStateStatus(statusName)
            viewModel.currentState.deviceId = deviceId
        }
    }

    override fun initListeners(dialog: Dialog) {
        btnClose.setSafeOnClickListener {
            dismiss()
        }
        btnSubmit.setSafeOnClickListener {
            viewModel.submitEditDevice()?.launchIn(lifecycleScope)
        }
        edtRoom.setSafeOnClickListener {
            roomBottomSheet.apply {
                currentRoomName = viewModel.currentState.currentRoom?.roomName ?: ""
            }.show(parentFragmentManager, null)
        }
        edtStatus.setSafeOnClickListener {
            statusBottomSheet.apply {
                currentStatusName = viewModel.currentState.currentStatus
            }.show(parentFragmentManager, null)
        }
        viewModel.store.apply {
            observe(
                owner = this@DetailDeviceDialog,
                selector = { state -> state.currentRoom },
                observer = {
                    it?.apply {
                        edtRoom.setText(it.roomName)
                    }
                })
            observe(
                owner = this@DetailDeviceDialog,
                selector = { state -> state.currentStatus },
                observer = {
                    edtStatus.setText(it)
                })
            observe(
                owner = this@DetailDeviceDialog,
                selector = { state -> state.stateEnableButtonSubmit },
                observer = {
                    btnSubmit.isEnabled = it
                })
            observe(
                owner = this@DetailDeviceDialog,
                selector = { state -> state.stateSubmitSuccess },
                observer = {
                    if (it == null) return@observe
                    if (it) {
                        Toast.makeText(requireContext(), "Successfully!!!", Toast.LENGTH_SHORT)
                            .show()
                        handleBackPress.invoke(
                            viewModel.currentState.currentRoom?.roomName ?: "",
                            viewModel.currentState.currentStatus
                        )
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Failed!!!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    viewModel.resetStateSubmit()
                })
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}