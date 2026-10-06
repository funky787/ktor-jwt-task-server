package org.example.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.example.dto.CreateTaskRequest
import org.example.dto.ErrorResponse
import org.example.dto.UpdateTaskRequest
import org.example.repository.TaskRepository

fun Route.taskRoutes() {
    route("/tasks") {
        get {
            call.respond(HttpStatusCode.OK, TaskRepository.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неверный id"))
                return@get
            }

            val task = TaskRepository.getById(id)

            if (task == null) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Задача не найдена"))
                return@get
            }

            call.respond(HttpStatusCode.OK, task)
        }

        authenticate("auth-jwt") {
            post {
                val request = call.receive<CreateTaskRequest>()

                if (request.title.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Название задачи не должно быть пустым"))
                    return@post
                }

                val task = TaskRepository.create(request)
                call.respond(HttpStatusCode.Created, task)
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неверный id"))
                    return@put
                }

                val request = call.receive<UpdateTaskRequest>()

                if (request.title.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Название задачи не должно быть пустым"))
                    return@put
                }

                val task = TaskRepository.update(id, request)

                if (task == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Задача не найдена"))
                    return@put
                }

                call.respond(HttpStatusCode.OK, task)
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неверный id"))
                    return@delete
                }

                if (!TaskRepository.delete(id)) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Задача не найдена"))
                    return@delete
                }

                call.response.status(HttpStatusCode.NoContent)
            }
        }
    }
}
