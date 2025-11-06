package ru.itmo.highload_systems_lab_1.mappers

import org.mapstruct.*
import ru.itmo.highload_systems_lab_1.database.entities.AccessKey
import ru.itmo.highload_systems_lab_1.database.entities.User
import ru.itmo.highload_systems_lab_1.database.repositories.UserRepository
import ru.itmo.highload_systems_lab_1.dtos.*
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
abstract class AccessKeyMapper {
    
    @Autowired
    private lateinit var userRepository: UserRepository

    @Mapping(target = "usagesCount", ignore = true)
    @Mapping(target = "userLogin", source = "userLogin", qualifiedByName = ["findUserByLogin"])
    abstract fun toEntity(createAccessKeyDTO: CreateAccessKeyDTO): AccessKey

    @Mapping(target = "key", ignore = true)
    @Mapping(target = "userLogin", ignore = true)
    @Mapping(target = "usagesCount", ignore = true)
    abstract fun toEntity(updateAccessKeyDTO: UpdateAccessKeyDTO, @MappingTarget accessKey: AccessKey)

    @Mapping(target = "userLogin", source = "userLogin.login")
    abstract fun toReadDTO(accessKey: AccessKey): ReadAccessKeyDTO

    @Named("findUserByLogin")
    fun findUserByLogin(login: String): User {
        return userRepository.findById(login).orElseThrow { 
            RuntimeException("User with login $login not found") 
        }
    }
}
