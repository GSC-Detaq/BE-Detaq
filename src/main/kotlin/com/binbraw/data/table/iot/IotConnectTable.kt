package com.binbraw.data.table.iot

import org.jetbrains.exposed.sql.Table

object IotConnectTable:Table("iot_connect") {
    val code = varchar("code", 16)
    val status_code = integer("status_code")
    val uid = varchar("uid", 128)
}