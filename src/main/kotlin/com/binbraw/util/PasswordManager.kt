package com.binbraw.util

import io.ktor.util.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.security.MessageDigest

object PasswordManager:KoinComponent {
    val config by inject<Config>()
    fun hashPassword(password:String):String{
        val bytes = (config.pw_salt + password + config.pw_salt).toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-512")
        val digest = md.digest(bytes)
        return hex(digest)
    }

    fun checkPassword(password: String, hashedPassword:String):Boolean{
        val bytes = (config.pw_salt + password + config.pw_salt).toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-512")
        val digest = md.digest(bytes)
        return hex(digest) == hashedPassword
    }
}