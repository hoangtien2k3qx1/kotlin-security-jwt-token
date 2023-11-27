package com.hoangtien2k3.userservice.controller

import com.hoangtien2k3.userservice.http.HeaderGenerator
import com.hoangtien2k3.userservice.model.dto.request.SignInForm
import com.hoangtien2k3.userservice.model.entity.User
import com.hoangtien2k3.userservice.repository.IUserRepository
import com.hoangtien2k3.userservice.security.jwt.JwtProvider
import com.hoangtien2k3.userservice.service.impl.UserServiceImpl
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*

/**
 * @author: hoangtien2k3
 * @create: 27/11/2023 - 17:28
 * @file: InformationUserController.kt
 * @update: 27/11/2023
 * @description:  api/information
 */
@RestController
@RequestMapping("api/information")
class InformationUserController @Autowired constructor(
    val userRepository: IUserRepository? = null,
    val headerGenerator: HeaderGenerator? = null,
    val jwtProvider: JwtProvider? = null,
    val authenticationManager: AuthenticationManager? = null,
    val userService: UserServiceImpl? = null
) {

    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null

    // get all user profile
    @GetMapping(value = ["/user/{username}"]) //    @PreAuthorize(value = "hasAuthority('ADMIN')")
    fun getUserByUsername(@PathVariable("username") username: String?): ResponseEntity<*> {
        val user = userRepository!!.getUserByUsername(username!!)
        return ResponseEntity<Any?>(
            user,
            headerGenerator?.getHeadersForSuccessGetMethod(),
            HttpStatus.OK
        )
    }

    // users/{id}
    @GetMapping(value = ["/user/{id}"])
    fun getUserById(@PathVariable("id") id: Long?): ResponseEntity<*> {
        if (userService!!.findById(id).isPresent) {
            val user = userService!!.findById(id).get()
            return ResponseEntity<Any?>(
                user,
                headerGenerator?.getHeadersForSuccessGetMethod(),
                HttpStatus.OK
            )
        }
        return ResponseEntity<Any?>(
            null,
            headerGenerator?.getHeadersForError(),
            HttpStatus.NOT_FOUND
        )
    }

    @GetMapping("/user/all")
    fun getAllUsers(): ResponseEntity<*> {
        val listUsers = userService!!.getAllUsers()
            .orElseThrow {
                UsernameNotFoundException(
                    "Not Found List User"
                )
            }
        return ResponseEntity<Any?>(
            listUsers,
            headerGenerator?.getHeadersForSuccessGetMethod(),
            HttpStatus.OK
        )
    }

    @GetMapping("/user/page")
    fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Any?> {
        val users = userService!!.getAllUsers(page, size)
        return ResponseEntity<Any?>(
            users,
            headerGenerator?.getHeadersForSuccessGetMethod(),
            HttpStatus.OK
        )
    }

    @GetMapping("/generate/token")
    fun getToken(@RequestBody signInForm: SignInForm): ResponseEntity<String> {
        val authentication = authenticationManager!!.authenticate(
            UsernamePasswordAuthenticationToken(signInForm.username, signInForm.password)
        )

        // Đoạn mã ở đây để lấy token từ hệ thống xác thực (nếu cần)
        val token = jwtProvider!!.createToken(authentication)

        // Trả về token trong phản hồi
        return ResponseEntity.ok(token)
    }

    @GetMapping("/token")
    fun getUsernameFromToken(@RequestParam("token") token: String?): String {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body
            .subject
    }


}