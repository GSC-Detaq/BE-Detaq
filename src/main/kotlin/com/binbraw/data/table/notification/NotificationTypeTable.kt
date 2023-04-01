package com.binbraw.data.table.notification

import org.jetbrains.exposed.sql.Table

object NotificationTypeTable:Table("notification_type") {
    val notif_type_id = integer("notif_type_id")
    val notif_type_word = varchar("notif_type_word", 64)
}