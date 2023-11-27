package com.hoangtien2k3.userservice.model.entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.hibernate.annotations.NaturalId

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    val name: RoleName
) {

}