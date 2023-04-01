package com.binbraw.model.response.iot

import kotlinx.serialization.Serializable

@Serializable
data class GetIotUidByCodeResponse(
    val uid:String
)
