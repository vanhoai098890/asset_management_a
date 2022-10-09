package com.example.assetmanagementapp.ui.navigationcontainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.app_common.ui.drawflower.ElasticDrawer
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentNavigationContainerBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainNavigationContainerFragment : BaseFragment() {
    private var binding: FragmentNavigationContainerBinding? = null
    private val viewModel: MainNavigationContainerViewModel by viewModels()
    private val fragmentPagers: MutableList<PagerContainerFragment> by lazy {
        mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNavigationContainerBinding.inflate(inflater, container, false)
        binding?.run {
            data = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        initViews()
        return binding?.root
    }

    private fun initViews() {
        binding?.drawerlayout?.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL)
    }

    private fun initFragments() {
        MainNavigationItem.values().forEachIndexed { index, _ ->
            if (index == MainNavigationItem.TEMP.ordinal) {
                return@forEachIndexed
            }
            fragmentPagers.add(PagerContainerFragment.newInstance(index).apply {
                onCallApiComplete = {
                }
            })
        }
    }
}
