package com.hoangtien2k3.userservice.security.userprinciple

import com.fasterxml.jackson.annotation.JsonIgnore
import com.hoangtien2k3.userservice.model.entity.User
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class UserPrinciple(
    val id: Long,
    val name: String,
    val username: String,
    val email: String,

    @JsonIgnore
    val password: String,
    val avatar: String,
    val roles: Collection<GrantedAuthority>

) : UserDetails {

    companion object {
        fun build(user: User): UserPrinciple {
            val authorityList: List<GrantedAuthority> = user.roles
                .stream()
                .map {
                        role -> SimpleGrantedAuthority(role.name.name)
                }
                .collect(Collectors.toList())

            return UserPrinciple(
                user.id,
                user.name,
                user.username,
                user.email,
                user.password,
                user.avatar,
                authorityList
            )
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority>? {
        return roles
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String? {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}