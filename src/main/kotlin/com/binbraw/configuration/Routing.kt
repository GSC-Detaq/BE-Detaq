package com.binbraw.configuration

import com.binbraw.data.api.emergency_contact.EmContactApi.addNewEmergencyContact
import com.binbraw.data.api.emergency_contact.EmContactApi.getAllEmergencyContact
import com.binbraw.data.api.emergency_contact.EmContactApi.getEmergencyContactByContactId
import com.binbraw.data.api.family.PatientWithFamilyApi.addNewFamily
import com.binbraw.data.api.fcm.FcmApi.sendFamiliesPushNotification
import com.binbraw.data.api.fcm.FcmApi.sendPushNotificationNoBody
import com.binbraw.data.api.fcm.FcmApi.updateFcmToken
import com.binbraw.data.api.general.role.RoleApi.getAllRole
import com.binbraw.data.api.general.role.RoleApi.getRoleById
import com.binbraw.data.api.general.role.RoleApi.newRole
import com.binbraw.data.api.iot.IotApi.addIotPairRequest
import com.binbraw.data.api.iot.IotApi.androidPairWithIot
import com.binbraw.data.api.iot.IotApi.getIotUidByCode
import com.binbraw.data.api.notification.NotificationApi.addNewDoctorReminderNotification
import com.binbraw.data.api.notification.NotificationApi.addNewMedicineReminderNotification
import com.binbraw.data.api.notification.NotificationApi.addSosNotification
import com.binbraw.data.api.reminder.DoctorReminderApi.addNewDoctorReminder
import com.binbraw.data.api.reminder.DoctorReminderApi.endStatusDoctorReminder
import com.binbraw.data.api.reminder.DoctorReminderApi.getAllDoctorReminder
import com.binbraw.data.api.reminder.MedicineReminderApi.addNewMedicineReminder
import com.binbraw.data.api.reminder.MedicineReminderApi.endStatusMedicineReminder
import com.binbraw.data.api.reminder.MedicineReminderApi.getAllMedicineReminder
import com.binbraw.data.api.user.UserApi.getMyOwnUserInfo
import com.binbraw.data.api.user.UserApi.login
import com.binbraw.data.api.user.UserApi.register
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureRegularRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        register("/user/register")
        login("/user/login")
        newRole("/role/new")
        getAllRole("/role/all")
        getRoleById("/role/getbyid")
        addIotPairRequest("/iot/request")
        getIotUidByCode("/iot/getuid")
        sendPushNotificationNoBody("/fcm/sendnotif_no_body")
    }
}

fun Application.configureAuthorizedRouting(){
    routing {
        authenticate("jwt-auth") {
            getMyOwnUserInfo("/user/myuser")
            addNewEmergencyContact("/emcontact/new")
            getEmergencyContactByContactId("/emcontact/bycontactid")
            getAllEmergencyContact("/emcontact/allemcontact")
            addNewMedicineReminder("/med_reminder/add")
            getAllMedicineReminder("/med_reminder/all")
            endStatusMedicineReminder("/med_reminder/end")
            addNewDoctorReminder("/doc_reminder/add")
            getAllDoctorReminder("/doc_reminder/all")
            endStatusDoctorReminder("/doc_reminder/end")
            addNewFamily("/family/add")
            updateFcmToken("/fcm/update")
            sendFamiliesPushNotification("/fcm/sendnotif/families")
            androidPairWithIot("/iot/androidpair")
            addNewMedicineReminderNotification("/notif/add/reminder/medicine")
            addNewDoctorReminderNotification("/notif/add/reminder/doctor")
            addSosNotification("/notif/add/sos")
        }
    }
}
