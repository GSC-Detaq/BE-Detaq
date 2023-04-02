package com.binbraw.data.api.notification

import com.binbraw.data.table.family.PatientWithFamilyTable
import com.binbraw.data.table.notification.NotificationTable
import com.binbraw.data.table.notification.NotificationTypeTable
import com.binbraw.model.base.MetaResponse
import com.binbraw.model.request.notification.AddNewReminderRequest
import com.binbraw.model.request.notification.AddNewSosRequest
import com.binbraw.model.response.notification.GetActiveNotificationCountResponse
import com.binbraw.model.response.notification.GetActiveNotificationDataResponse
import com.binbraw.model.response.notification.GetAllNotificationResponse
import com.binbraw.model.response.notification.SingleNotificationResponse
import com.binbraw.wrapper.sendGeneralResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

object NotificationApi : KoinComponent {
    val notifTable by inject<NotificationTable>()
    val notifTypeTable by inject<NotificationTypeTable>()
    val patientFamilyTable by inject<PatientWithFamilyTable>()

    fun Route.addNewMedicineReminderNotification(path: String) {
        post(path) {
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

    fun Route.addNewDoctorReminderNotification(path: String) {
        post(path) {
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

    fun Route.addSosNotification(path: String) {
        post(path) {
            val body = call.receive<AddNewSosRequest>()
            val uid = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asString()

            val familyIds = transaction {
                patientFamilyTable.select {
                    patientFamilyTable.patient_id eq UUID.fromString(uid)
                }.mapNotNull {
                    it[patientFamilyTable.family_id]
                }
            }

            familyIds.map { id ->
                val randomizedNotifId = UUID.randomUUID()
                transaction {
                    notifTable.insert {
                        it[notifTable.notification_id] = randomizedNotifId
                        it[notifTable.title] = body.title
                        it[notifTable.body] = body.body
                        it[notifTable.additional_link] = body.additional_link
                        it[notifTable.timestamp] = body.timestamp
                        it[notifTable.uid] = uid
                        it[notifTable.lat] = body.lat
                        it[notifTable.long] = body.long
                        it[notifTable.notif_type_id] = 3
                    }
                }
            }
//            val randomizedNotifId = UUID.randomUUID()
//            transaction {
//                notifTable.insert {
//                    it[notifTable.notification_id] = randomizedNotifId
//                    it[notifTable.title] = body.title
//                    it[notifTable.body] = body.body
//                    it[notifTable.additional_link] = body.additional_link
//                    it[notifTable.timestamp] = body.timestamp
//                    it[notifTable.uid] = uid
//                    it[notifTable.notif_type_id] = 3
//                }
//            }.let {
//                sendGeneralResponse<Any>(
//                    success = true,
//                    message = "Add SOS notification success",
//                    code = HttpStatusCode.OK
//                )
//            }
        }
    }

    fun Route.getAllNotification(path: String) {
        get(path) {
            val uid = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asString()
            val page = call.parameters["page"]?.toIntOrNull() ?: 1
            val perPage = 5
            val offset = (page - 1) * perPage

            val notifTypes = transaction {
                notifTypeTable.selectAll().mapNotNull {
                    it[notifTypeTable.notif_type_id] to it[notifTypeTable.notif_type_word]
                }
            }.toMap()

            val datas = transaction {
                notifTable
                    .select { notifTable.uid eq uid and (notifTable.visible eq 1) }
                    .orderBy(notifTable.no, SortOrder.DESC)
                    .limit(perPage, offset.toLong())
                    .mapNotNull {
                        SingleNotificationResponse(
                            notification_id = it[notifTable.notification_id].toString(),
                            title = it[notifTable.title],
                            body = it[notifTable.body],
                            additional_link = it[notifTable.additional_link],
                            timestamp = it[notifTable.timestamp],
                            clicked = when (it[notifTable.clicked]) {
                                0 -> false
                                1 -> true
                                else -> false
                            },
                            uid = it[notifTable.uid],
                            notif_type = notifTypes[it[notifTable.notif_type_id]] ?: "Unknown type",
                            lat = it[notifTable.lat],
                            long = it[notifTable.long]
                        )
                    }
            }

            call.respond(
                HttpStatusCode.OK,
                GetAllNotificationResponse(
                    meta = MetaResponse(
                        success = true,
                        message = "Get notification at page $page"
                    ),
                    page = page,
                    data = datas
                )
            )
        }
    }

    fun Route.setClickedStatusToTwo(path:String){
        get(path){
            val uid = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asString()
            val notifId = call.parameters["notification_id"] ?: ""

            transaction {
                notifTable.update(
                    {
                        notifTable.notification_id eq UUID.fromString(notifId) and (notifTable.uid eq uid)
                    }
                ) {
                    it[notifTable.clicked] = 1
                }
            }.let {
                sendGeneralResponse<Any>(
                    success = true,
                    message = "Click status for $notifId changed",
                    code = HttpStatusCode.OK
                )
            }
        }
    }

    fun Route.getActiveNotificationCount(path:String){
        get(path){
            val uid = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asString()

            val count = transaction {
                notifTable.select {
                    notifTable.uid eq uid and (notifTable.clicked eq 0)
                }.count()
            }

            call.respond(
                HttpStatusCode.OK,
                GetActiveNotificationCountResponse(
                    meta = MetaResponse(
                        success = true,
                        message = "Get active notification success"
                    ),
                    data = GetActiveNotificationDataResponse(
                        active_notif_count = count
                    )
                )
            )
        }
    }
}