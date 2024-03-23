package com.example.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.model.Userdata

class Jwt_Service {
    private val issuer = "note_issuer"
    private val secret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()
    fun generateToken(user: Userdata): String{
        return JWT.create()
            .withSubject("Note Authentication")
            .withIssuer(issuer)
            .withClaim("email",user.email)
            .sign(algorithm)
    }
}