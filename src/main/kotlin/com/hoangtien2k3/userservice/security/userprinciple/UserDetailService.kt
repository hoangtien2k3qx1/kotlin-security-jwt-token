package com.hoangtien2k3.userservice.security.userprinciple

import com.hoangtien2k3.userservice.model.entity.User
import com.hoangtien2k3.userservice.repository.IUserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.function.Supplier

@Service
class UserDetailService : UserDetailsService {

    @Autowired
    private var userRepository: IUserRepository? = null

    // check user exists in DB
    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository!!.findByUsername(username)
            .orElseThrow(Supplier {
                UsernameNotFoundException(
                    "User not found, username and password: $username"
                )
            })
        return UserPrinciple.build(user)
    }

    @Transactional
    @Throws(UsernameNotFoundException::class)
    fun loadUserByEmail(email: String): UserDetails {
        val user: User = userRepository!!.findByEmail(email)
            .orElseThrow(Supplier {
                UsernameNotFoundException(
                    "User not found, email and password: $email"
                )
            })
        return UserPrinciple.build(user)
    }

    @Transactional
    @Throws(UsernameNotFoundException::class)
    fun loadUserByPhone(phone: String): UserDetails {
        val user: User = userRepository!!.findByEmail(phone)
            .orElseThrow(Supplier {
                UsernameNotFoundException(
                    "User not found, phone and password: $phone"
                )
            })
        return UserPrinciple.build(user)
    }
}