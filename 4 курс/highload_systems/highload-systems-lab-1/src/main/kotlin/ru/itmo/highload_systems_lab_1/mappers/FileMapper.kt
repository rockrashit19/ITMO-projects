package ru.itmo.highload_systems_lab_1.mappers

import org.mapstruct.*
import ru.itmo.highload_systems_lab_1.database.entities.File
import ru.itmo.highload_systems_lab_1.database.entities.Project
import ru.itmo.highload_systems_lab_1.database.entities.User
import ru.itmo.highload_systems_lab_1.database.repositories.ProjectRepository
import ru.itmo.highload_systems_lab_1.database.repositories.UserRepository
import ru.itmo.highload_systems_lab_1.dtos.*
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
abstract class FileMapper {
    
    @Autowired
    private lateinit var projectRepository: ProjectRepository
    
    @Autowired
    private lateinit var userRepository: UserRepository

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projectId", source = "projectId", qualifiedByName = ["findProjectById"])
    @Mapping(target = "uploaderUserLogin", source = "uploaderUserLogin", qualifiedByName = ["findUserByLogin"])
    abstract fun toEntity(createFileDTO: CreateFileDTO): File

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "uploaderUserLogin", ignore = true)
    abstract fun toEntity(updateFileDTO: UpdateFileDTO, @MappingTarget file: File)

    @Mapping(target = "projectId", source = "projectId.id")
    @Mapping(target = "uploaderUserLogin", source = "uploaderUserLogin.login")
    abstract fun toReadDTO(file: File): ReadFileDTO

    @Named("findProjectById")
    fun findProjectById(id: Int): Project {
        return projectRepository.findById(id).orElseThrow { 
            RuntimeException("Project with id $id not found") 
        }
    }

    @Named("findUserByLogin")
    fun findUserByLogin(login: String): User {
        return userRepository.findById(login).orElseThrow { 
            RuntimeException("User with login $login not found") 
        }
    }
}
