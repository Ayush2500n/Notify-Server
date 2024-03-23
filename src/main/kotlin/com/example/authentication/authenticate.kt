package com.example.authentication

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

    private val hashkey = System.getenv("HASHKEY").toByteArray()
    private val hmackey = SecretKeySpec(hashkey,"HmacSHA1")
fun hash(password: String): String{
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmackey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}