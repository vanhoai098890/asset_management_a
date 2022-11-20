package com.example.assetmanagementapp.data.remote.api.datasource.department

import com.example.app_common.base.exception.apiCall
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.department.AddDepartmentItemResponse
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentAdditionRequest
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentItemResponse
import javax.inject.Inject

class DepartmentDatasourceImpl @Inject constructor(private val apiService: ApiService) :
    DepartmentDatasource {

    override suspend fun getAllDepartment(): DepartmentItemResponse {
        return apiCall {
            apiService.getDepartments()
        }
    }

    override suspend fun addDepartment(name: String): AddDepartmentItemResponse {
        return apiCall {
            apiService.addDepartment(DepartmentAdditionRequest(nameDepartment = name))
        }
    }
}
