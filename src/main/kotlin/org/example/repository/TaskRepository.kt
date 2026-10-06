package org.example.repository

import org.example.dto.CreateTaskRequest
import org.example.dto.UpdateTaskRequest
import org.example.model.Task

object TaskRepository {
    private val tasks = mutableListOf(
        Task(1, "Изучить Ktor", "Разобраться с маршрутами", false),
        Task(2, "Сделать REST API", "Добавить CRUD для задач", true)
    )

    private var nextId = 3

    fun getAll(): List<Task> = tasks.toList()

    fun getById(id: Int): Task? = tasks.find { it.id == id }

    fun create(request: CreateTaskRequest): Task {
        val task = Task(
            id = nextId++,
            title = request.title,
            description = request.description,
            done = request.done
        )
        tasks.add(task)
        return task
    }

    fun update(id: Int, request: UpdateTaskRequest): Task? {
        val index = tasks.indexOfFirst { it.id == id }
        if (index == -1) return null

        val updatedTask = Task(
            id = id,
            title = request.title,
            description = request.description,
            done = request.done
        )

        tasks[index] = updatedTask
        return updatedTask
    }

    fun delete(id: Int): Boolean {
        return tasks.removeIf { it.id == id }
    }
}
