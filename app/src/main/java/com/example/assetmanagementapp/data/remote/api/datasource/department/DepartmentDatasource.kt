package com.example.assetmanagementapp.data.remote.api.datasource.department

import com.example.assetmanagementapp.data.remote.api.model.department.AddDepartmentItemResponse
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentItemResponse

interface DepartmentDatasource {
    suspend fun getAllDepartment(): DepartmentItemResponse

    suspend fun addDepartment(name: String): AddDepartmentItemResponse
}
