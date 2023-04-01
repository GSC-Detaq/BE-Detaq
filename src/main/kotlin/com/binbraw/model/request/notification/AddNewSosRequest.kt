package com.binbraw.model.request.notification

import kotlinx.serialization.Serializable

@Serializable
data class AddNewSosRequest(
    val title:String,
    val body:String,
    val additional_link:String,
    val timestamp:String
)
