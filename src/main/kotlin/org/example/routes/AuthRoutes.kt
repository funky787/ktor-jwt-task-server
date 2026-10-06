package org.example.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.dto.ErrorResponse
import org.example.dto.LoginRequest
import org.example.dto.RegisterRequest
import org.example.dto.TokenResponse
import org.example.dto.UserResponse
import org.example.repository.UserRepository
import org.example.security.JwtConfig
import org.mindrot.jbcrypt.BCrypt

fun Route.authRoutes() {
    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()

            if (request.login.isBlank() || request.password.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Логин и пароль не должны быть пустыми")
                )
                return@post
            }

            if (request.password.length < 4) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Пароль должен содержать минимум 4 символа")
                )
                return@post
            }

            if (UserRepository.findByLogin(request.login) != null) {
                call.respond(
                    HttpStatusCode.Conflict,
                    ErrorResponse("Пользователь с таким логином уже существует")
                )
                return@post
            }

            val passwordHash = BCrypt.hashpw(request.password, BCrypt.gensalt())
            val user = UserRepository.create(request.login, passwordHash)

            call.respond(
                HttpStatusCode.Created,
                UserResponse(user.id, user.login)
            )
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val user = UserRepository.findByLogin(request.login)

            if (user == null || !BCrypt.checkpw(request.password, user.passwordHash)) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse("Неверный логин или пароль")
                )
                return@post
            }

            val token = JwtConfig.generateToken(user)

            call.respond(
                HttpStatusCode.OK,
                TokenResponse(
                    token = token,
                    expiresInSeconds = JwtConfig.expirationTime / 1000,
                    userId = user.id
                )
            )
        }
    }
}
