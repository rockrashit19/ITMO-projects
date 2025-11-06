package ru.itmo.highload_systems_lab_1.mappers

import org.mapstruct.*
import ru.itmo.highload_systems_lab_1.database.entities.User
import ru.itmo.highload_systems_lab_1.dtos.*
import ru.itmo.highload_systems_lab_1.utils.Role

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
abstract class UserMapper {
    companion object {
        private val BCRYPT = BCryptPasswordEncoder()
    }

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "invitedCount", ignore = true)
    abstract fun toEntity(createUserDTO: CreateUserDTO): User

    @Mapping(target = "login", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "invitedCount", ignore = true)
    abstract fun toEntity(updateUserDTO: UpdateUserDTO, @MappingTarget user: User)

    abstract fun toReadDTO(user: User): ReadUserDTO

    @AfterMapping
    fun encodePassword(createUserDTO: CreateUserDTO, @MappingTarget user: User) {
        user.passwordHash = BCRYPT.encode(createUserDTO.password)
    }

    @AfterMapping
    fun setDefaults(createUserDTO: CreateUserDTO, @MappingTarget user: User) {
        user.role = Role.USER
        user.invitedCount = 0
    }

    @AfterMapping
    fun encodePassword(updateUserDTO: UpdateUserDTO, @MappingTarget user: User) {
        user.passwordHash = BCRYPT.encode(updateUserDTO.password)
    }
}
