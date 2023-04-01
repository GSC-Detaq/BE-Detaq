package com.binbraw.data.api.iot

import com.binbraw.data.api.fcm.FcmApi
import com.binbraw.data.table.iot.IotConnectTable
import com.binbraw.model.request.fcm.SendPushNotificationRequestAsClient
import com.binbraw.model.request.fcm.SendPushNotificationRequestAsClientData
import com.binbraw.model.response.iot.GetIotUidByCodeResponse
import com.binbraw.wrapper.sendGeneralResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.header
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

object IotApi : KoinComponent {
    val iotConnectTable by inject<IotConnectTable>()

    fun Route.addIotPairRequest(path: String) {
        get(path) {
            val code = call.parameters["code"] ?: ""

            if (code.isEmpty()) {
                sendGeneralResponse<Any>(
                    success = false,
                    message = "Insert code on your request",
                    code = HttpStatusCode.BadRequest
                )
                return@get
            }

            transaction {
                iotConnectTable.insert {
                    it[iotConnectTable.code] = code
                    it[iotConnectTable.status_code] = 1
                }
            }.let {
                sendGeneralResponse<Any>(
                    success = true,
                    message = "Pair request added",
                    code = HttpStatusCode.OK
                )
            }
        }
    }

    fun Route.androidPairWithIot(path: String) {
        get(path) {
            val uid = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asString()
            val code = call.parameters["code"] ?: ""

            if (code.isEmpty()) {
                sendGeneralResponse<Any>(
                    success = false,
                    message = "Insert code on your request",
                    code = HttpStatusCode.BadRequest
                )
                return@get
            }

            val codeStatus = try {
                transaction {
                    iotConnectTable.select {
                        iotConnectTable.code eq code
                    }.mapNotNull { it[iotConnectTable.status_code] }
                }[0]
            } catch (e: Exception) {
                -1
            }

            when (codeStatus) {
                -1 -> {
                    sendGeneralResponse<Any>(
                        success = false,
                        message = "Code not found, check your input",
                        code = HttpStatusCode.BadRequest
                    )
                    return@get
                }

                2 -> {
                    sendGeneralResponse<Any>(
                        success = false,
                        message = "Code has been used, try again",
                        code = HttpStatusCode.BadRequest
                    )
                    return@get
                }
            }

            transaction {
                iotConnectTable.update({
                    iotConnectTable.code eq code
                }) {
                    it[iotConnectTable.status_code] = 2
                    it[iotConnectTable.uid] = uid
                }
            }.let {
                sendGeneralResponse<Any>(
                    success = true,
                    message = "Connect success",
                    code = HttpStatusCode.OK
                )
                return@get
            }
        }
    }

    fun Route.getIotUidByCode(path: String) {
        get(path) {
            val code = call.parameters["code"] ?: ""

            val uid = try {
                transaction {
                    iotConnectTable.select {
                        iotConnectTable.code eq code
                    }.mapNotNull { it[iotConnectTable.uid] }
                }[0]
            } catch (e: Exception) {
                ""
            }

            if (uid.isNotEmpty()) {
                val client = HttpClient(CIO) {
                    install(ContentNegotiation) {
                        gson()
                    }
                }

                val token = try {
                    transaction {
                        FcmApi.fcmTokenTable.select {
                            FcmApi.fcmTokenTable.uid eq UUID.fromString(uid)
                        }.mapNotNull {
                            it[FcmApi.fcmTokenTable.token]
                        }
                    }[0]
                } catch (e: Exception) {
                    ""
                }

                client.post("https://fcm.googleapis.com/fcm/send") {
                    header("Authorization", "key=${FcmApi.config.fcm_access_key}")
                    setBody(
                        SendPushNotificationRequestAsClient(
                            to = token,
                            notification = SendPushNotificationRequestAsClientData(
                                body = "Congratulation, your device has been connected successfully",
                                title = "Device has been connected",
                                link = ""
                            )
                        )
                    )
                    contentType(ContentType.Application.Json)
                }

                call.respond(
                    HttpStatusCode.OK,
                    GetIotUidByCodeResponse(
                        uid = uid
                    )
                )
            } else {
                sendGeneralResponse<Any>(
                    success = false,
                    message = "Pair on your phone first",
                    code = HttpStatusCode.BadRequest
                )
                return@get
            }
        }
    }
}