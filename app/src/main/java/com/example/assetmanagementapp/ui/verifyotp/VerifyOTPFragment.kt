package com.example.assetmanagementapp.ui.verifyotp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.base.exception.DefaultError
import com.example.app_common.base.response.ApiResponseCode
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentVerifyOtpBinding
import com.example.assetmanagementapp.ui.forgotpassword.ChangePasswordSuccessFragment
import com.example.assetmanagementapp.ui.forgotpassword.SetPasswordTypeFlow
import com.example.assetmanagementapp.utils.toPhoneNumberDisplay
import com.example.assetmanagementapp.utils.toTimeDisplay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerifyOTPFragment : BaseFragment() {
    companion object {
        private const val KEY_INPUT_PHONE = "key_input_phone"
        private const val KEY_PASSWORD = "key_password"
        private const val KEY_TYPE_FLOW = "KEY_TYPE_FLOW"
        internal fun newInstance(inputPhone: String, password: String, typeFlow: Int) =
            VerifyOTPFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(KEY_INPUT_PHONE, inputPhone)
                        putString(KEY_PASSWORD, password)
                        putInt(KEY_TYPE_FLOW, typeFlow)
                    }
                }
    }

    private val viewModel: VerifyOTPViewModel by viewModels()
    private var binding: FragmentVerifyOtpBinding? = null

    private fun showTemporarilyDisableAccountDialog() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_otp, container, false)
        binding?.run {
            data = viewModel
            lifecycleOwner = this@VerifyOTPFragment.viewLifecycleOwner
        }
        initData()
        initViews()
        initEvents()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countDownReSendOTP()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initData() {
        arguments?.let {
            viewModel.phoneNumber = it.getString(
                KEY_INPUT_PHONE
            ) ?: ""
            viewModel.password = it.getString(
                KEY_PASSWORD
            ) ?: ""
            viewModel.typeFlow = it.getInt(KEY_TYPE_FLOW, -1)
        }
    }

    private fun initViews() {
        binding?.run {
            layoutVerifyOTPToolbar.tvCenter.text =
                getString(R.string.s010003_verify_otp_header)
            layoutVerifyOTPToolbar.backButton.visibility = View.VISIBLE
            tvVerifyOTPDescription.text =
                getString(
                    R.string.s010003_verify_otp_des,
                    viewModel.phoneNumber.toPhoneNumberDisplay()
                )
            edtVerifyOTPEntry.requestFocusOTP()
        }
    }

    private fun initEvents() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.loadingState().collect {
                    handleShowLoadingDialog(it)
                }
            }
        }
        binding?.run {
            layoutVerifyOTPToolbar.backButton.setOnClickListener {
                handleBackPressed()
            }
            edtVerifyOTPEntry.otpFullyListener = {
                viewModel.apiToVerifyOTP(it, viewModel.typeFlow)
                    .onSuccess {
                        if (viewModel.typeFlow == SetPasswordTypeFlow.FLOW_SIGN_UP.ordinal) {
                            handleVerifyOTPSignUpSuccess()
                        } else {
                            handleVerifyOTPResetPasswordSuccess()
                        }
                    }.onError {
                        edtVerifyOTPEntry.showError()
                    }.launchIn(lifecycleScope)
            }
            edtVerifyOTPEntry.onOTPChangeListener = {
                viewModel.stateViewError.value = View.GONE
            }
            btnVerifyOTPResend.setSafeOnClickListener {
                viewModel.apiToResendOTP().onSuccess {
                    countDownReSendOTP()
                }.onError { errorResponse ->
                    (errorResponse as? DefaultError)?.let { error ->
                        if (error.apiErrorCode == ApiResponseCode.E037) {
                            showTemporarilyDisableAccountDialog()
                        }
                    }
                }.launchIn(lifecycleScope)
            }
        }
    }

    private fun countDownReSendOTP() {
        viewModel.initCountDownResendOTP().onEach {
            binding?.btnVerifyOTPResend?.text =
                getString(R.string.s010003_verify_otp_resend_count_down, it.toTimeDisplay())
        }.onCompletion {
            binding?.btnVerifyOTPResend?.text =
                getString(R.string.s010003_verify_otp_to_resend_otp)
        }.launchIn(lifecycleScope)
    }

    private fun handleVerifyOTPSignUpSuccess() {}

    private fun handleVerifyOTPResetPasswordSuccess() {
        addFragmentNotMain(ChangePasswordSuccessFragment(), false)
    }
}
