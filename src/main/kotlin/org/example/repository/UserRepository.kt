package org.example.repository

import org.example.model.User

object UserRepository {
    private val users = mutableListOf<User>()
    private var nextId = 1

    fun findByLogin(login: String): User? {
        return users.find { it.login.equals(login, ignoreCase = true) }
    }

    fun findById(id: Int): User? {
        return users.find { it.id == id }
    }

    fun create(login: String, passwordHash: String): User {
        val user = User(
            id = nextId++,
            login = login,
            passwordHash = passwordHash
        )
        users.add(user)
        return user
    }
}
