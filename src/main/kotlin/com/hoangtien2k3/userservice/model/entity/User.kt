package com.hoangtien2k3.userservice.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import lombok.*
import org.hibernate.annotations.NaturalId

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["username"]), UniqueConstraint(columnNames = ["email"])]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @NotBlank
    @Size(min = 3, max = 50)
    var name: String,

    @NotBlank
    @Size(min = 3, max = 50)
    var username: String,

    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Email
    var email: String,

    @JsonIgnore
    @NotBlank
    @Size(min = 6, max = 100)
    var password: String,

    @Lob
    var avatar: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = HashSet()
) {

}
