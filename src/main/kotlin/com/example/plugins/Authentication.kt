package com.example.plugins

import com.example.authentication.Jwt_Service
import com.example.repository.repo
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuthentication(){
    log.info("Authentication started")
    val jwtService = Jwt_Service()
    val db = repo()
    install(Authentication){
        jwt("jwt"){
            verifier(jwtService.verifier)
            realm = "Notify Server"
            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = db.findUser(email)
                user
            }
        }
    }
}