package com.example.plugins

import com.example.authentication.Jwt_Service
import com.example.authentication.hash
import com.example.data.model.Userdata
import com.example.repository.repo
import com.example.routes.NoteRoutes
import com.example.routes.UserRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    log.info("Routing started")

    val db = repo()
    val hashfun = {s: String -> hash(s) }
    val jwt_service = Jwt_Service()
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
//        get("/token") {
//            val email = call.request.queryParameters["email"]!!
//            val name = call.request.queryParameters["name"]!!
//            val password = call.request.queryParameters["password"]!!
//            val user = Userdata(email,name, hash(password))
//            call.respond(jwt_service.generateToken(user))
//        }
        UserRoutes(db,jwt_service,hashfun)
        NoteRoutes(db)
    }
}
