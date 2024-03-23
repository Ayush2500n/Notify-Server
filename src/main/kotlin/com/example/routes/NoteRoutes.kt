package com.example.routes

import com.example.data.model.Note
import com.example.data.model.SimpleResponse
import com.example.data.model.Userdata
import com.example.repository.repo
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post

const val NOTES = "$API_VERSION/notes"
const val CREATE_NOTE = "$NOTES/create"
const val UPDATE_NOTE = "$NOTES/update"
const val DELETE_NOTE = "$NOTES/delete"

@Resource(NOTES)
class GetNotes

@Resource(CREATE_NOTE)
class CreateNote

@Resource(UPDATE_NOTE)
class UpdateNote

@Resource(DELETE_NOTE)
class DeleteNote

fun Route.NoteRoutes(db: repo){
    authenticate("jwt") {
        post("v1/notes/create") {
            val note = try {
                call.receive<Note>()
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing some fields"))
                return@post
            }
            try {
                val email = call.principal<Userdata>()!!.email
                db.addNote(note, email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true, "Note Added"))
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,e.message?:"Some error occurred"))
            }
        }
        delete("v1/notes/delete") {
            val noteid = try {
                call.request.queryParameters["id"]!!
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"No Note selected"))
                return@delete
            }
            try {
                val email = call.principal<Userdata>()!!.email
                db.deleteNote(id = noteid, email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true, "Note deleted"))
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,e.message?:"Some error occurred"))
            }
        }
        post("v1/notes/update") {
            val note = try {
                call.receive<Note>()
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing some fields"))
                return@post
            }
            try {
                val email = call.principal<Userdata>()!!.email
                db.updateNote(note, email, note.id)
                call.respond(HttpStatusCode.OK,SimpleResponse(true, "Note Updated"))
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,e.message?:"Some error occurred"))
            }
        }
        get("v1/notes") {
           try {
               val email = call.principal<Userdata>()!!.email
               val notes = db.getAllNotes(email)
               call.respond(HttpStatusCode.OK,notes)
           }catch (e:Exception){
               call.respond(HttpStatusCode.BadRequest, emptyList<Note>())
           }
        }
    }
}