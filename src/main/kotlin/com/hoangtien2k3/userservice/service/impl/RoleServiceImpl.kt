package com.hoangtien2k3.userservice.service.impl

import com.hoangtien2k3.userservice.model.entity.Role
import com.hoangtien2k3.userservice.model.entity.RoleName
import com.hoangtien2k3.userservice.repository.IRoleRepository
import com.hoangtien2k3.userservice.service.IRoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoleServiceImpl @Autowired constructor(private val roleRepository: IRoleRepository) : IRoleService {
    override fun findByName(name: RoleName): Optional<Role> {
        return roleRepository.findByName(name)
    }
}
