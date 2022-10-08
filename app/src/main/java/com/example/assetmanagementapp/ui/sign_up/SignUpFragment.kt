package com.example.assetmanagementapp.ui.sign_up

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.replaceFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseActivity
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentSignUpBinding
import com.example.assetmanagementapp.ui.onboard_welcome.OnBoardFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {
    private var binding: FragmentSignUpBinding? = null
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding?.apply {
            data = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        initData()
        initEvents()
        return binding?.root
    }

    private fun initEvents() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.statusNavToOnBoardScreen.collect {
                    if (it) {
                        (requireActivity() as? BaseActivity)?.replaceFragment(OnBoardFragment().apply {
                            setLevel(AppConstant.LEVEL_TAB)
                        }, true)
                        viewModel.statusNavToOnBoardScreen.value = false
                    }
                }
                viewModel.loadingState().collect {
                    handleShowLoadingDialog(it)
                }
            }
        }
    }

    private fun togglePasswordVisibility() {
        binding?.apply {
            if (viewModel.isPasswordVisible) {
                edtPassword.transformationMethod = PasswordTransformationMethod()
                ibTogglePassword.setImageResource(R.drawable.ic_password_toggle_invisible)
                edtPassword.setSelection(edtPassword.length())
            } else {
                edtPassword.transformationMethod = null
                ibTogglePassword.setImageResource(R.drawable.ic_password_toggle_visible)
                edtPassword.setSelection(edtPassword.length())
            }
            viewModel.isPasswordVisible = !viewModel.isPasswordVisible
        }
    }

    private fun clearInput(editText: EditText) {
        editText.setText(AppConstant.EMPTY)
        editText.requestFocus()
    }

    private fun initData() {
        binding?.apply {
            tvSignIn.setSafeOnClickListener {
                handleBackPressed()
            }
            btnBack.setSafeOnClickListener {
                handleBackPressed()
            }
            ibTogglePassword.setOnClickListener {
                togglePasswordVisibility()
            }
            ibClearPassword.setOnClickListener {
                clearInput(edtPassword)
            }
            ibClearUsername.setOnClickListener {
                clearInput(edtUsername)
            }
            ibClearName.setOnClickListener {
                clearInput(edtName)
            }
            ibClearEmail.setOnClickListener {
                clearInput(edtEmail)
            }
            ibClearPhone.setOnClickListener {
                clearInput(edtPhone)
            }
            ibClearCitizenId.setSafeOnClickListener {
                clearInput(edtCitizenId)
            }
        }
    }
}
