package com.binbraw.model.request.fcm

import kotlinx.serialization.Serializable

@Serializable
data class SendPushNotificationRequest(
    val longitude:String,
    val latitude:String,
    val timestamp:String
)
