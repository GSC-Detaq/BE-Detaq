package com.binbraw.model.response.notification

import com.binbraw.model.base.MetaResponse
import kotlinx.serialization.Serializable

@Serializable
data class GetAllNotificationResponse(
    val meta:MetaResponse,
    val page:Int,
    val data:List<SingleNotificationResponse>
)
