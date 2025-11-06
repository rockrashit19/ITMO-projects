package ru.itmo.highload_systems_lab_1.mappers

import org.mapstruct.*
import ru.itmo.highload_systems_lab_1.database.entities.Project
import ru.itmo.highload_systems_lab_1.database.entities.User
import ru.itmo.highload_systems_lab_1.database.repositories.UserRepository
import ru.itmo.highload_systems_lab_1.dtos.*
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
abstract class ProjectMapper {
    
    @Autowired
    private lateinit var userRepository: UserRepository

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creatorUserLogin", source = "creatorUserLogin", qualifiedByName = ["findUserByLogin"])
    abstract fun toEntity(createProjectDTO: CreateProjectDTO): Project

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creatorUserLogin", ignore = true)
    abstract fun toEntity(updateProjectDTO: UpdateProjectDTO, @MappingTarget project: Project)

    @Mapping(target = "creatorUserLogin", source = "creatorUserLogin.login")
    abstract fun toReadDTO(project: Project): ReadProjectDTO

    @Named("findUserByLogin")
    fun findUserByLogin(login: String): User {
        return userRepository.findById(login).orElseThrow { 
            RuntimeException("User with login $login not found") 
        }
    }
}
