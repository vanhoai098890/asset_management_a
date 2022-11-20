package com.example.assetmanagementapp.ui.department

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentItem
import com.example.assetmanagementapp.data.repositories.DepartmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class DepartmentViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository
) : BaseViewModelV2<DepartmentState>() {
    override fun initState(): DepartmentState {
        return DepartmentState()
    }

    init {
        departmentRepository.getAllDepartment().bindLoading(this).onSuccess {
            dispatchState(currentState.copy(listDepartment = ArrayList(it.data)))
        }.launchIn(viewModelScope)
    }

    fun addDepartment(nameDepartment: String) {
        departmentRepository.addDepartment(nameDepartment).bindLoading(this).onSuccess {
            dispatchState(
                currentState.copy(
                    listDepartment = ArrayList(currentState.listDepartment.apply {
                        add(it.data)
                    }),
                    stateAddDepartmentSuccess = true
                )
            )
        }.onError {
            dispatchState(currentState.copy(stateAddDepartmentSuccess = false))
        }.launchIn(viewModelScope)
    }
}

data class DepartmentState(
    val listDepartment: MutableList<DepartmentItem> = mutableListOf(),
    val stateAddDepartmentSuccess: Boolean? = null
)
