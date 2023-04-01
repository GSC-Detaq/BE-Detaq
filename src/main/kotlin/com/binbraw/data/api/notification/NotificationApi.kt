package com.binbraw.data.api.notification

import com.binbraw.data.table.notification.NotificationTable
import com.binbraw.data.table.notification.NotificationTypeTable
import com.binbraw.model.request.notification.AddNewReminderRequest
import com.binbraw.model.request.notification.AddNewSosRequest
import com.binbraw.wrapper.sendGeneralResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

object NotificationApi:KoinComponent {
    val notifTable by inject<NotificationTable>()
    val notifTypeTable by inject<NotificationTypeTable>()
    fun Route.addNewMedicineReminderNotification(path:String){
        post(path){
            val body = call.receive<AddNewReminderRequest>()
            val uid = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asString()
            val randomizedNotifId = UUID.randomUUID()

            transaction {
                notifTable.insert {
                    it[notifTable.notification_id] = randomizedNotifId
                    it[notifTable.title] = body.title
                    it[notifTable.body] = body.body
                    it[notifTable.timestamp] = body.timestamp
                    it[notifTable.uid] = uid
                    it[notifTable.notif_type_id] = 1
                }
            }.let {
                sendGeneralResponse<Any>(
                    success = true,
                    message = "Add medicine notification success",
                    code = HttpStatusCode.OK
                )
            }
        }
    }

    fun Route.addNewDoctorReminderNotification(path:String){
        post(path){
            val body = call.receive<AddNewReminderRequest>()
            val uid = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asString()
            val randomizedNotifId = UUID.randomUUID()

            transaction {
                notifTable.insert {
                    it[notifTable.notification_id] = randomizedNotifId
                    it[notifTable.title] = body.title
                    it[notifTable.body] = body.body
                    it[notifTable.timestamp] = body.timestamp
                    it[notifTable.uid] = uid
                    it[notifTable.notif_type_id] = 2
                }
            }.let {
                sendGeneralResponse<Any>(
                    success = true,
                    message = "Add doctor notification success",
                    code = HttpStatusCode.OK
                )
            }
        }
    }

    fun Route.addSosNotification(path:String){
        post(path){
            val body = call.receive<AddNewSosRequest>()
            val uid = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asString()
            val randomizedNotifId = UUID.randomUUID()

            transaction {
                notifTable.insert {
                    it[notifTable.notification_id] = randomizedNotifId
                    it[notifTable.title] = body.title
                    it[notifTable.body] = body.body
                    it[notifTable.additional_link] = body.additional_link
                    it[notifTable.timestamp] = body.timestamp
                    it[notifTable.uid] = uid
                    it[notifTable.notif_type_id] = 3
                }
            }.let {
                sendGeneralResponse<Any>(
                    success = true,
                    message = "Add SOS notification success",
                    code = HttpStatusCode.OK
                )
            }
        }
    }
//
//    fun Route.getAllNotification
}