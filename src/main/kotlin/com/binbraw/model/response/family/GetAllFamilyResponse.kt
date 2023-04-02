package com.binbraw.model.response.family

import com.binbraw.model.base.MetaResponse
import com.binbraw.model.response.user.UserDataResponse
import kotlinx.serialization.Serializable

@Serializable
data class GetAllFamilyResponse(
    val meta:MetaResponse,
    val data:List<GetAllFamilyResponseData>
)

@Serializable
data class GetAllFamilyResponseData(
    val uid:String,
    val email:String,
    val name:String,
    val role_name:String
)
