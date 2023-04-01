package com.binbraw.model.response.notification

import com.binbraw.model.base.MetaResponse
import kotlinx.serialization.Serializable

@Serializable
data class GetActiveNotificationCountResponse(
    val meta:MetaResponse,
    val data:GetActiveNotificationDataResponse
)

@Serializable
data class GetActiveNotificationDataResponse(
    val active_notif_count:Long
)
