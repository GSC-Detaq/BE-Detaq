package com.binbraw.model.request.notification

import kotlinx.serialization.Serializable

@Serializable
data class AddNewSosRequest(
    val title:String,
    val body:String,
    val additional_link:String,
    val lat:String,
    val long:String,
    val timestamp:String
)
