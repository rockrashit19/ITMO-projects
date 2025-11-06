package ru.itmo.highload_systems_lab_1.mappers

import org.mapstruct.*
import ru.itmo.highload_systems_lab_1.database.entities.UserProject
import ru.itmo.highload_systems_lab_1.database.entities.UserProjectId
import ru.itmo.highload_systems_lab_1.dtos.*

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
abstract class UserProjectMapper {

    @Mapping(target = "id", source = ".", qualifiedByName = ["createUserProjectId"])
    abstract fun toEntity(createUserProjectDTO: CreateUserProjectDTO): UserProject

    @Mapping(target = "id", ignore = true)
    abstract fun toEntity(updateUserProjectDTO: UpdateUserProjectDTO, @MappingTarget userProject: UserProject)

    @Mapping(target = "userLogin", source = "id.userLogin")
    @Mapping(target = "projectId", source = "id.projectId")
    abstract fun toReadDTO(userProject: UserProject): ReadUserProjectDTO

    @Named("createUserProjectId")
    fun createUserProjectId(dto: CreateUserProjectDTO): UserProjectId {
        return UserProjectId(dto.userLogin, dto.projectId)
    }
}
