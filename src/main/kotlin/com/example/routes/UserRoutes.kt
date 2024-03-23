package com.example.routes

import com.example.authentication.Jwt_Service
import com.example.data.model.LoginRequest
import com.example.data.model.RegistrationRequest
import com.example.data.model.SimpleResponse
import com.example.data.model.Userdata
import com.example.repository.repo
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.log

const val API_VERSION = "v1"
const val USERS = "$API_VERSION/users"
const val REGISTER = "$USERS/register"
const val LOGIN = "$USERS/login"

@Resource(REGISTER)
class Registration

@Resource(LOGIN)
class Login

fun Route.UserRoutes(db: repo, jwtService: Jwt_Service, hash: (String) -> String){
    post("v1/users/register") {
        val register_request = try {
            call.receive<RegistrationRequest>()
        }catch (e:Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false,"Missing some fields"))
            return@post
        }
        try {
            val user = Userdata(register_request.email,register_request.name,hash(register_request.password))
            db.addUser(user)
            call.respond(HttpStatusCode.OK,SimpleResponse(success = true,jwtService.generateToken(user)))
        }catch (e:Exception){
            call.respond(HttpStatusCode.Conflict,SimpleResponse(success = false,"Some error occured!"))
        }
    }
    post("v1/users/login") {
        val login_request = try {
            call.receive<LoginRequest>()
        }catch (e:Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false,"Missing some fields"))
            return@post
        }
        try {
            val user = db.findUser(email = login_request.email)
            if (user == null)
            {
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(success = false,"No user found"))
            }
            else{
                if (user.hashpassword == hash(login_request.password)){
                    call.respond(HttpStatusCode.OK,SimpleResponse(success = true,jwtService.generateToken(user)))
                }else{
                    println("Stored Hashed Password: ${user.hashpassword}")
                    println("Received Hashed Password: ${hash(login_request.password)}")
                    call.respond(HttpStatusCode.BadRequest,SimpleResponse(success = false, "Incorrect Password"))
                }
            }
        }catch (e:Exception){
            call.respond(HttpStatusCode.Conflict,SimpleResponse(success = false,"Some error occured!"))
        }
    }
}