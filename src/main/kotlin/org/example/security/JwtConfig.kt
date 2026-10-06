package org.example.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.example.model.User
import java.util.Date

object JwtConfig {
    private val secret = System.getenv("JWT_SECRET") ?: "simple-secret-for-study-project"
    private const val issuer = "ktor-task-server"
    private const val audience = "ktor-users"

    const val realm = "Ktor Task Server"
    const val expirationTime = 60 * 60 * 1000L

    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    fun generateToken(user: User): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", user.id)
            .withClaim("login", user.login)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
            .sign(algorithm)
    }
}
