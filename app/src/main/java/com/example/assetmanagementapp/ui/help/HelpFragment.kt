package com.example.assetmanagementapp.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentHelpBinding
import com.example.assetmanagementapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpFragment : BaseFragment() {

    private lateinit var binding: FragmentHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.apply {
            layoutToolbar.apply {
                root.setBackgroundColor(resources.getColor(R.color.white, null))
                tvCenter.text = getString(R.string.v1_help)
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
            }

            layoutCardHotLine.tvCall.apply {
                setSafeOnClickListener {
                    Utils.callHotLine(context, text.toString())
                }
            }
            btSendEmail.setSafeOnClickListener {
                Utils.sendEmail(context, arguments?.getString(ACCOUNT_NAME) ?: "")
            }
        }
    }

    companion object {
        private const val ACCOUNT_NAME = "ACCOUNT_NAME"
        fun newInstance(accountName: String): HelpFragment {
            val args = Bundle().apply {
                putString(ACCOUNT_NAME, accountName)
            }
            val fragment = HelpFragment()
            fragment.arguments = args
            return fragment
        }
    }

}