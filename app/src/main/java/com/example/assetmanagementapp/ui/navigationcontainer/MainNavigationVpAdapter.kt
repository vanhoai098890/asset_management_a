package com.example.assetmanagementapp.ui.navigationcontainer

import androidx.fragment.app.Fragment
import com.example.app_common.base.BaseAdapterViewPager2
import com.example.assetmanagementapp.common.BaseFragment

class MainNavigationVpAdapter(
    fragment: Fragment,
    levelParent: Int,
    fragmentPagers: List<BaseFragment>
) : BaseAdapterViewPager2(fragment, levelParent, fragmentPagers)
