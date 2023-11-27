package com.hoangtien2k3.userservice.controller

import com.hoangtien2k3.userservice.model.dto.model.TokenManager
import com.hoangtien2k3.userservice.repository.IUserRepository
import com.hoangtien2k3.userservice.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * @author: hoangtien2k3
 * @create: 27/11/2023 - 17:28
 * @file: SendToken.kt
 * @update: 27/11/2023
 * @description: /api/manager
 */
@Service
@RequestMapping("/api/manager")
class SendToken @Autowired constructor(
    val userService: IUserService? = null,
    val userRepository: IUserRepository? = null,
    val tokenManager: TokenManager? = null
) {
    @GetMapping("/token/{username}")
    fun getTokenByUsername(@PathVariable("username") username: String?): ResponseEntity<String> {
        val user = userRepository!!.findByUsername(username!!)
            .orElseThrow {
                UsernameNotFoundException(
                    "Username not found."
                )
            }

        val token = tokenManager!!.getTokenByUsername(username)
        return if (token != null) {
            ResponseEntity.ok(token)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found for the username.")
        }
    }

    @GetMapping("/token")
    fun getToken(): ResponseEntity<String> {
        val token = tokenManager!!.TOKEN
        return if (token != null) {
            ResponseEntity.ok(token)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found for the username.")
        }
    }
}