package com.binbraw.model.response.notification

import kotlinx.serialization.Serializable
import javax.management.monitor.StringMonitor

@Serializable
data class SingleNotificationResponse(
    val notification_id:String,
    val title:String,
    val body:String,
    val additional_link:String,
    val timestamp:String,
    val clicked:Boolean,
    val uid:String,
    val notif_type:String
)