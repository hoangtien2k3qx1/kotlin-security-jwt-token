package com.hoangtien2k3.userservice.repository

import com.hoangtien2k3.userservice.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IUserRepository : JpaRepository<User, Long> {
    fun findByUsername(name: String): Optional<User>
    fun findByEmail(name: String): Optional<User>
    override fun findById(id: Long): Optional<User>
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun getUserByUsername(username: String): User
    override fun findAll(): MutableList<User>
}