package com.example.assetmanagementapp.ui.securityaccount

import android.os.Bundle
import android.text.Editable
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.utils.text_watcher.TextWatcherImpl
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentChangePasswordBinding
import com.example.assetmanagementapp.ui.setpassword.SetPasswordViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment() {

    private lateinit var binding: FragmentChangePasswordBinding
    private val viewModel: SetPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        binding.apply {
            data = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        initView()
        initData()
        initEvents()
        return binding.root
    }

    private fun initEvents() {
        viewModel.loadingState().onEach {
            handleShowLoadingDialog(it)
        }.launchIn(lifecycleScope)
        lifecycleScope.launchWhenStarted {
            viewModel.stateSuccess.collect {
                it?.apply {
                    if (this) {
                        Toast.makeText(
                            requireContext(),
                            "Password has been successfully changed!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        handleBackPressed()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Some thing wrong when change password!!\n Please try again later",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun initData() {
        arguments?.apply {
            viewModel.userPhoneNumber.value = getString(PHONE_NUMBER) ?: ""
        }
    }

    private fun initView() {
        activity?.apply {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        binding.apply {
            layoutCommonToolbarAuthen.apply {
                tvCenter.text = resources.getString(R.string.s01004_change_password_setting)
                backButton.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        handleBackPressed()
                    }
                }
            }
            btnSetPassword.setSafeOnClickListener {
                checkTypeFlowAndRedirect()
            }
            ibToggleCurrentPassword.setOnClickListener {
                toggleCurrentPasswordVisibility()
            }
            ibTogglePassword.setOnClickListener {
                togglePasswordVisibility()
            }
            ibToggleRePassword.setOnClickListener {
                toggleRePasswordVisibility()
            }
            ibClearCurrentPassword.setOnClickListener {
                clearTextPasswordInput(edtCurrentPassword)
            }
            ibClearPassword.setOnClickListener {
                clearTextPasswordInput(edtPassword)
            }
            ibClearRePassword.setOnClickListener {
                clearTextPasswordInput(edtReTypePassword)
            }
            edtCurrentPassword.setOnFocusChangeListener { _, hasFocus ->
                handleFocusChange(
                    hasFocus,
                    edtCurrentPassword,
                    viewModel.currentPassword.value,
                    ibClearCurrentPassword
                )
            }
            edtPassword.setOnFocusChangeListener { _, hasFocus ->
                handleFocusChange(
                    hasFocus,
                    edtPassword,
                    viewModel.password.value,
                    ibClearPassword
                )
            }

            edtReTypePassword.setOnFocusChangeListener { _, hasFocus ->
                handleFocusChange(
                    hasFocus,
                    edtReTypePassword,
                    viewModel.password.value,
                    ibClearRePassword
                )
            }
        }
    }

    private fun checkTypeFlowAndRedirect() {
        viewModel.changePassword()
    }

    private fun toggleCurrentPasswordVisibility() {
        if (viewModel.isCurrentPasswordVisible) {
            binding.edtCurrentPassword.transformationMethod = PasswordTransformationMethod()
            binding.ibToggleCurrentPassword.setImageResource(R.drawable.ic_password_toggle_invisible)
            binding.edtCurrentPassword.setSelection(viewModel.currentPassword.value.length)
        } else {
            binding.edtCurrentPassword.transformationMethod = null
            binding.ibToggleCurrentPassword.setImageResource(R.drawable.ic_password_toggle_visible)
            binding.edtCurrentPassword.setSelection(viewModel.currentPassword.value.length)
        }
        viewModel.isPasswordVisible = !viewModel.isPasswordVisible
    }

    private fun togglePasswordVisibility() {
        if (viewModel.isPasswordVisible) {
            binding.edtPassword.transformationMethod = PasswordTransformationMethod()
            binding.ibTogglePassword.setImageResource(R.drawable.ic_password_toggle_invisible)
            binding.edtPassword.setSelection(viewModel.password.value.length)
        } else {
            binding.edtPassword.transformationMethod = null
            binding.ibTogglePassword.setImageResource(R.drawable.ic_password_toggle_visible)
            binding.edtPassword.setSelection(viewModel.password.value.length)
        }
        viewModel.isPasswordVisible = !viewModel.isPasswordVisible
    }

    private fun toggleRePasswordVisibility() {
        if (viewModel.isRePasswordVisible) {
            binding.edtReTypePassword.transformationMethod = PasswordTransformationMethod()
            binding.ibToggleRePassword.setImageResource(R.drawable.ic_password_toggle_invisible)
            binding.edtReTypePassword.setSelection(viewModel.confirmPassword.value.length)
        } else {
            binding.edtReTypePassword.transformationMethod = null
            binding.ibToggleRePassword.setImageResource(R.drawable.ic_password_toggle_visible)
            binding.edtReTypePassword.setSelection(viewModel.confirmPassword.value.length)
        }
        viewModel.isRePasswordVisible = !viewModel.isRePasswordVisible
    }

    private fun clearTextPasswordInput(textInputEditText: TextInputEditText?) {
        textInputEditText?.setText(AppConstant.EMPTY)
        textInputEditText?.requestFocus()
    }

    private fun handleTextChange(
        textInputEditText: TextInputEditText?,
        clearButton: ImageButton?
    ) {
        textInputEditText?.addTextChangedListener(object : TextWatcherImpl() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                if (s.toString().isNotEmpty()) {
                    clearButton?.visibility = View.VISIBLE
                } else {
                    clearButton?.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun handleFocusChange(
        isFocus: Boolean,
        textInputEditText: TextInputEditText?,
        currentValue: String,
        clearButton: ImageButton?
    ) {
        if (isFocus) {
            if (currentValue.isNotEmpty()) {
                clearButton?.visibility = View.VISIBLE
            } else {
                clearButton?.visibility = View.INVISIBLE
            }
            handleTextChange(textInputEditText, clearButton)
        } else {
            clearButton?.visibility = View.INVISIBLE
        }
    }

    companion object {
        private const val PHONE_NUMBER = "PHONE_NUMBER"
        fun newInstance(phoneNumber: String): ChangePasswordFragment {
            val args = Bundle().apply {
                putString(PHONE_NUMBER, phoneNumber)
            }
            val fragment = ChangePasswordFragment()
            fragment.arguments = args
            return fragment
        }
    }

}