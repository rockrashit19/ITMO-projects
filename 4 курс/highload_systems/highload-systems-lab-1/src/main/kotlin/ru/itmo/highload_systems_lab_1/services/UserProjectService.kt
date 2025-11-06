package ru.itmo.highload_systems_lab_1.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.itmo.highload_systems_lab_1.database.entities.UserProjectId
import ru.itmo.highload_systems_lab_1.database.repositories.UserProjectRepository
import ru.itmo.highload_systems_lab_1.dtos.CreateUserProjectDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadUserProjectDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateUserProjectDTO
import ru.itmo.highload_systems_lab_1.mappers.UserProjectMapper
import ru.itmo.highload_systems_lab_1.exceptions.UserProjectNotFoundException
import ru.itmo.highload_systems_lab_1.services.util.pageRequest

@Service
class UserProjectService(
    private val userProjectRepository: UserProjectRepository,
    private val userProjectMapper: UserProjectMapper
) {

    fun createUserProject(dto: CreateUserProjectDTO): ReadUserProjectDTO {
        val entity = userProjectMapper.toEntity(dto)
        val saved = userProjectRepository.save(entity)
        return userProjectMapper.toReadDTO(saved)
    }

    fun getUserProject(userLogin: String, projectId: Int): ReadUserProjectDTO {
        val id = UserProjectId(userLogin, projectId)
        val userProject = userProjectRepository.findById(id).orElseThrow { 
            UserProjectNotFoundException(userLogin, projectId) 
        }
        return userProjectMapper.toReadDTO(userProject)
    }

    fun updateUserProject(userLogin: String, projectId: Int, dto: UpdateUserProjectDTO): ReadUserProjectDTO {
        val id = UserProjectId(userLogin, projectId)
        val userProject = userProjectRepository.findById(id).orElseThrow { 
            UserProjectNotFoundException(userLogin, projectId) 
        }
        userProjectMapper.toEntity(dto, userProject)
        val saved = userProjectRepository.save(userProject)
        return userProjectMapper.toReadDTO(saved)
    }

    fun deleteUserProject(userLogin: String, projectId: Int) {
        val id = UserProjectId(userLogin, projectId)
        if (!userProjectRepository.existsById(id)) {
            throw UserProjectNotFoundException(userLogin, projectId)
        }
        userProjectRepository.deleteById(id)
    }

    fun findAll(page: Int, size: Int): Page<ReadUserProjectDTO> {
        val pageable = pageRequest(page, size)
        return userProjectRepository.findAll(pageable).map { userProjectMapper.toReadDTO(it) }
    }
}
