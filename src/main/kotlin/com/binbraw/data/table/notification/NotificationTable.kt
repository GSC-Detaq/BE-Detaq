package com.binbraw.data.table.notification

import org.jetbrains.exposed.sql.Table

object NotificationTable:Table("notification") {
    val notification_id = uuid("notification_id")
    val notif_type_id = integer("notif_type_id")
    val title = varchar("title", 128)
    val body = varchar("body", 128)
    val additional_link = varchar("additional_link", 128)
    val timestamp = varchar("timestamp", 64)
    val visible = integer("visible")
    val clicked = integer("clicked")
    val uid = varchar("uid", 128)
    val lat = varchar("lat", 64)
    val long = varchar("long", 64)
    val no = integer("no")
}