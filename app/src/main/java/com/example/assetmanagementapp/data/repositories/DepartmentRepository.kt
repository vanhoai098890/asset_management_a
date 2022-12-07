package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.department.DepartmentDatasourceImpl
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentDetailRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepartmentRepository @Inject constructor(
    private val departmentDatasourceImpl: DepartmentDatasourceImpl,
) {
    fun getAllDepartment() = safeFlow {
        departmentDatasourceImpl.getAllDepartment()
    }

    fun addDepartment(nameDepartment: String) = safeFlow {
        departmentDatasourceImpl.addDepartment(nameDepartment)
    }

    fun getRoomByDepartmentId(departmentDetailRequest: DepartmentDetailRequest) = safeFlow {
        departmentDatasourceImpl.getRoomByDepartmentId(departmentDetailRequest)
    }

    fun getRooms() = safeFlow {
        departmentDatasourceImpl.getRooms()
    }

}
