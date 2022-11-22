package com.example.assetmanagementapp.data.remote.api.datasource.department

import com.example.assetmanagementapp.data.remote.api.model.department.AddDepartmentItemResponse
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentDetailRequest
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentItemResponse
import com.example.assetmanagementapp.data.remote.api.model.room.ListRoomItemResponse

interface DepartmentDatasource {
    suspend fun getAllDepartment(): DepartmentItemResponse

    suspend fun addDepartment(name: String): AddDepartmentItemResponse

    suspend fun getRoomByDepartmentId(departmentDetailRequest: DepartmentDetailRequest): ListRoomItemResponse
}
