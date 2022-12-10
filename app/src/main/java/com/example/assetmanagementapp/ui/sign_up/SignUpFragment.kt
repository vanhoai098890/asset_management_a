package com.example.assetmanagementapp.ui.sign_up

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.databinding.FragmentSignUpBinding
import com.example.assetmanagementapp.ui.signupbottomsheet.CommonSignUpBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    private val commonSignUpBottomSheet: CommonSignUpBottomSheet by lazy {
        CommonSignUpBottomSheet()
    }
    var handleOnBackPress: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        binding.apply {
            data = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        initData()
        initEvents()
        return binding.root
    }

    private fun initEvents() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.statusNavToOnBoardScreen.collect {
                    if (it) {
                        Toast.makeText(requireContext(), "Successfully!!", Toast.LENGTH_SHORT)
                            .show()
                        handleBackPressed()
                    }
                }
            }
            launch {
                viewModel.loadingState().collect {
                    handleShowLoadingDialog(it)
                }
            }
            launch {
                viewModel.stateCity.collect {
                    it?.apply {
                        binding.edtCity.setText(it.typeName)
                    }
                }
            }
            launch {
                viewModel.stateCountry.collect {
                    it?.apply {
                        binding.edtCountry.setText(it.typeName)
                    }
                }
            }
            launch {
                viewModel.stateRole.collect {
                    it?.apply {
                        binding.edtRole.setText(it.typeName)
                    }
                }
            }
            launch {
                viewModel.stateMajor.collect {
                    it?.apply {
                        binding.edtMajor.setText(it.typeName)
                    }
                }
            }
            launch {
                viewModel.stateBirthday.collect {
                    binding.edtBirthday.setText(it)
                }
            }
        }
    }

    private fun togglePasswordVisibility() {
        binding.apply {
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

        binding.apply {
            btnBack.setSafeOnClickListener {
                handleBackPressed()
            }
            ibTogglePassword.setOnClickListener {
                togglePasswordVisibility()
            }
            ibClearPassword.setOnClickListener {
                clearInput(edtPassword)
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
            ibClearAddress.setSafeOnClickListener {
                clearInput(edtAddress)
            }
            edtCity.setSafeOnClickListener {
                viewModel.stateListCity.value.let { listCity ->
                    if (listCity.isNotEmpty()) {
                        commonSignUpBottomSheet.apply {
                            currentStatusName = viewModel.stateCity.value?.typeName ?: ""
                            listTypeAsset = viewModel.stateListCity.value
                            statusOnClick = {
                                viewModel.stateCity.value = it
                            }
                        }.show(parentFragmentManager, null)
                    }
                }
            }
            edtCountry.setSafeOnClickListener {
                viewModel.stateListCountry.value.let { listCountry ->
                    if (listCountry.isNotEmpty()) {
                        commonSignUpBottomSheet.apply {
                            currentStatusName = viewModel.stateCountry.value?.typeName ?: ""
                            listTypeAsset = viewModel.stateListCountry.value
                            statusOnClick = {
                                viewModel.stateCountry.value = it
                            }
                        }.show(parentFragmentManager, null)
                    }
                }
            }
            edtMajor.setSafeOnClickListener {
                viewModel.stateListMajor.value.let { listMajor ->
                    if (listMajor.isNotEmpty()) {
                        commonSignUpBottomSheet.apply {
                            currentStatusName = viewModel.stateMajor.value?.typeName ?: ""
                            listTypeAsset = viewModel.stateListMajor.value
                            statusOnClick = {
                                viewModel.stateMajor.value = it
                            }
                        }.show(parentFragmentManager, null)
                    }
                }
            }
            edtRole.setSafeOnClickListener {
                commonSignUpBottomSheet.apply {
                    currentStatusName = viewModel.stateRole.value?.typeName ?: ""
                    listTypeAsset = viewModel.stateListRole.value
                    statusOnClick = {
                        viewModel.stateRole.value = it
                    }
                }.show(parentFragmentManager, null)
            }
            val myCalendar = Calendar.getInstance()
            edtBirthday.setSafeOnClickListener {
                val tempYear = myCalendar.get(Calendar.YEAR)
                val tempMonth = myCalendar.get(Calendar.MONTH)
                val tempDate = myCalendar.get(Calendar.DAY_OF_MONTH)
                DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        viewModel.stateBirthday.value = String.format(
                            "%04d-%02d-%02d",
                            year,
                            month,
                            dayOfMonth
                        )
                    },
                    tempYear,
                    tempMonth,
                    tempDate
                ).show()
            }
            arguments?.apply {
                getParcelable<UserInfo>(USER_INFO)?.apply {
                    viewModel.dispatchStateUserInfo(this)
                    tvWelcome.text = getString(R.string.v1_edit_account)
                    tvSignUp.text = getString(R.string.v1_edit)
                    edtName.setText(username)
                    edtEmail.setText(email)
                    edtCitizenId.setText(cmnd)
                    edtAddress.setText(address)
                    edtPhone.setText(phoneNumber)
                    ibClearPhone.visibility = View.GONE
                    ibClearPhone.isClickable = false
                    edtPhone.isClickable = false
                    edtPhone.isEnabled = false
                }
            }
        }
    }

    override fun handleBackPressed(tagNameBackStack: String?) {
        handleOnBackPress.invoke()
        super.handleBackPressed(tagNameBackStack)
    }

    companion object {
        private const val USER_INFO = "USER_INFO"
        fun newInstance(userInfo: UserInfo): SignUpFragment {
            val args = Bundle().apply {
                putParcelable(USER_INFO, userInfo)
            }
            val fragment = SignUpFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
