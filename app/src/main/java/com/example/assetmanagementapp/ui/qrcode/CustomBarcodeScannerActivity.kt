package com.example.assetmanagementapp.ui.qrcode

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.utils.LogUtils
import com.example.assetmanagementapp.BuildConfig
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.databinding.ActivityCustomBarcodeScanerBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import java.net.URL


@FlowPreview
@AndroidEntryPoint
class CustomBarcodeScannerActivity : AppCompatActivity(), DecoratedBarcodeView.TorchListener {
    companion object {
        private const val FLASH_ON = "FLASH_ON"
        private const val FLASH_OFF = "FLASH_OFF"
        const val DEVICE_ID_RESULT = "DEVICE_ID_RESULT"

        // Use to check if the host of the URL scanned by the user's QR code is from canow or not (for red envelope event)
        // Pattern to check string is a numeric
        private val pattern: String =
            BuildConfig.HOST_DETAIL_DEVICE + "detail_device/"
    }

    private lateinit var capture: CustomCaptureManager
    private lateinit var binding: ActivityCustomBarcodeScanerBinding
    private val viewModel: CustomBarcodeScannerViewModel by viewModels()
    private var activityPermissionResult: ActivityResultLauncher<Intent>

    init {
        activityPermissionResult = registerActivityResult()
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null) {
                // Prevent duplicate scans
                return
            }
            try {
                LogUtils.d("BarcodeCallback ${result.text}")
                if (!isValidHost(result.text)) throw Exception("Invalid Host")
                result.text.replace(pattern, "").toInt()?.apply {
                    viewModel.checkDeviceExist(this)
                }
            } catch (e: Exception) {
                binding.tvErrorLabel.visibility = View.VISIBLE
                binding.tvErrorLabel.text = getString(R.string.s6_1_1_2_scan_merchant_qr)
                binding.bcScanner.barcodeView.resume()
            }
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) = Unit
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViews()
        initEvents()
        capture = CustomCaptureManager(this, binding.bcScanner) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri =
                Uri.fromParts(
                    "package",
                    packageName,
                    null
                )
            intent.data = uri
            activityPermissionResult.launch(intent)
        }
        capture.apply {
            initializeFromIntent(intent, savedInstanceState)
            val formats: MutableList<BarcodeFormat> = ArrayList()
            formats.add(BarcodeFormat.QR_CODE)
            binding.bcScanner.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
            binding.bcScanner.barcodeView.decodeContinuous(callback)
        }
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        binding.bcScanner.barcodeView.pause()
        capture.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return binding.bcScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    override fun onTorchOn() {
        binding.barcodeToolbar.endIcon.setImageResource(R.drawable.ic_flash_on)
    }

    override fun onTorchOff() {
        binding.barcodeToolbar.endIcon.setImageResource(R.drawable.ic_flash_off)
    }

    private fun registerActivityResult(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val grantResult = ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            )
            if (grantResult != PackageManager.PERMISSION_GRANTED) capture.displayCameraPermissionDialog()
        }
    }

    private fun initBinding() {
        binding = ActivityCustomBarcodeScanerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initViews() {
        binding.barcodeToolbar.apply {
            root.setBackgroundColor(resources.getColor(R.color.transparent, null))
            tvCenter.apply {
                text = getString(R.string.scan_qr_code)
                tvCenter.setTextColor(resources.getColor(R.color.white, null))
            }
            backButton.apply {
                drawable.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
                setOnClickListener {
                    finish()
                }
            }
            displayFlashButton(hasFlash())
        }
        binding.bcScanner.let {
            it.setTorchListener(this)
            it.viewFinder.setLaserVisibility(false)
        }
    }

    private fun initEvents() {
        binding.barcodeToolbar.endIcon.setSafeOnClickListener {
            switchFlashlight(binding.barcodeToolbar.endIcon)
        }
        viewModel.store.apply {
            observe(
                owner = this@CustomBarcodeScannerActivity,
                selector = { state -> state.stateDeviceExist },
                observer = { isExist ->
                    when (isExist) {
                        null -> {
                            binding.tvErrorLabel.visibility = View.GONE
                        }
                        true -> {
                            setResult(Activity.RESULT_OK, Intent().apply {
                                putExtra(
                                    AppConstant.SCAN_QR_RESULT,
                                    ScanQRCodeResult(viewModel.currentState.stateCurrentDeviceId.toString())
                                )
                            })
                            finish()
                        }
                        false -> {
                            binding.tvErrorLabel.apply {
                                text = getString(R.string.s6_1_1_2_scan_merchant_qr)
                                visibility = View.VISIBLE
                            }
                        }
                    }
                })
        }

    }

    private fun switchFlashlight(view: View) {
        if (view.tag == FLASH_OFF) {
            view.tag = FLASH_ON
            binding.bcScanner.setTorchOn()
        } else {
            view.tag = FLASH_OFF
            binding.bcScanner.setTorchOff()
        }
    }

    private fun displayFlashButton(flash: Boolean) {
        binding.barcodeToolbar.endIcon.apply {
            visibility = if (flash) {
                View.VISIBLE
            } else {
                View.GONE
            }
            tag = FLASH_OFF
        }
    }

    private fun hasFlash(): Boolean {
        return applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    private fun isValidHost(url: String): Boolean {
        return URL(url).host == URL(BuildConfig.HOST_DETAIL_DEVICE).host
    }
}
