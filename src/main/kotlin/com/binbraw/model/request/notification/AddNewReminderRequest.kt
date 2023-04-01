package com.binbraw.model.request.notification

import kotlinx.serialization.Serializable

@Serializable
data class AddNewReminderRequest(
    val title:String,
    val body:String,
    val timestamp:String
)
