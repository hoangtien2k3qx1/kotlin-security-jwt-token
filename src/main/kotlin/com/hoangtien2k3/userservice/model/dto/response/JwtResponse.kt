package com.hoangtien2k3.userservice.model.dto.response

import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.Setter
import org.springframework.security.core.GrantedAuthority

@Getter
@Setter
@RequiredArgsConstructor
class JwtResponse(
    var accessToken: String?,
    var refreshToken: String?,
    var id: Long?,
    var name: String?,
    var roles: Collection<GrantedAuthority?>?
) {
    val type = "Bearer"
}
