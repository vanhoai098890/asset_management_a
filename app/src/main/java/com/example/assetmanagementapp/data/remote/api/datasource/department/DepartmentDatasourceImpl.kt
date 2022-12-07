package com.example.assetmanagementapp.data.remote.api.datasource.department

import com.example.app_common.base.exception.apiCall
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.department.AddDepartmentItemResponse
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentAdditionRequest
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentDetailRequest
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentItemResponse
import com.example.assetmanagementapp.data.remote.api.model.room.ListRoomItemResponse
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

    override suspend fun getRoomByDepartmentId(departmentDetailRequest: DepartmentDetailRequest): ListRoomItemResponse {
        return apiCall {
            apiService.getRoomsByDepartmentId(departmentDetailRequest)
        }
    }

    override suspend fun getRooms(): ListRoomItemResponse {
        return apiCall {
            apiService.getRooms()
        }
    }
}
