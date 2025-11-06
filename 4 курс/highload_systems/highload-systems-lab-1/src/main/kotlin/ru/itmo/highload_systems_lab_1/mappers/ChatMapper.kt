package ru.itmo.highload_systems_lab_1.mappers

import org.mapstruct.*
import ru.itmo.highload_systems_lab_1.database.entities.Chat
import ru.itmo.highload_systems_lab_1.database.entities.Project
import ru.itmo.highload_systems_lab_1.database.repositories.ProjectRepository
import ru.itmo.highload_systems_lab_1.dtos.*
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
abstract class ChatMapper {
    
    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projectId", source = "projectId", qualifiedByName = ["findProjectById"])
    abstract fun toEntity(createChatDTO: CreateChatDTO): Chat

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projectId", ignore = true)
    abstract fun toEntity(updateChatDTO: UpdateChatDTO, @MappingTarget chat: Chat)

    @Mapping(target = "projectId", source = "projectId.id")
    abstract fun toReadDTO(chat: Chat): ReadChatDTO

    @Named("findProjectById")
    fun findProjectById(id: Int): Project {
        return projectRepository.findById(id).orElseThrow { 
            RuntimeException("Project with id $id not found") 
        }
    }
}
