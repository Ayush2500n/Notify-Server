package com.example.repository

import com.example.data.model.Note
import com.example.data.model.Userdata
import com.example.data.table.NoteTable
import com.example.data.table.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class repo {
    suspend fun addUser(user: Userdata) {
        DatabaseFactory.dbquery {
            UserTable.insert { ut ->
                ut[name] = user.name
                ut[hashpassword] = user.hashpassword
                ut[email] = user.email
            }
        }
    }

    suspend fun findUser(email: String) = DatabaseFactory.dbquery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowtoUser(it) }
            .singleOrNull()
    }

    private fun rowtoUser(row: ResultRow): Userdata? {
        if (row == null) {
            return null
        }
        return Userdata(
            email = row[UserTable.email],
            name = row[UserTable.name],
            hashpassword = row[UserTable.hashpassword]
        )
    }
/////////////////////////////////////////////////////////////////NOTES//////////////////////////////////////////////////////////////////////////////////////////////////////

    suspend fun addNote(note: Note, Email: String) {
        DatabaseFactory.dbquery {
            NoteTable.insert {
                it[id] = note.id
                it[title] = note.title
                it[description] = note.description
                it[email] = Email
                it[date] = note.date
            }
        }
    }

    suspend fun deleteNote(id: String, email: String) {
        DatabaseFactory.dbquery {
            NoteTable.deleteWhere {
                NoteTable.id.eq(id) and NoteTable.email.eq(email)
            }
        }
    }

    suspend fun updateNote(note: Note, email: String, id: String) {
        DatabaseFactory.dbquery {
            NoteTable.update(where = { NoteTable.email.eq(email) and  NoteTable.id.eq(id) }) {
                it[title] = note.title
                it[description] = note.description
                it[date] = note.date
            }
        }
    }

    suspend fun getAllNotes(email: String): List<Note> = DatabaseFactory.dbquery {
        NoteTable.select {
            NoteTable.email.eq(email)
        }.mapNotNull { rowToNote(it) }
    }

    private fun rowToNote(row: ResultRow?): Note? {
        if (row == null) {
            return null
        }
        return Note(
            title = row[NoteTable.title],
            description = row[NoteTable.description],
            date = row[NoteTable.date],
            id = row[NoteTable.id],
        )
    }
}