package ru.itmo.highload_systems_lab_1.controllers

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.itmo.highload_systems_lab_1.dtos.CreateUserProjectDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadUserProjectDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateUserProjectDTO
import ru.itmo.highload_systems_lab_1.services.UserProjectService
import ru.itmo.highload_systems_lab_1.utils.API_URL

@RestController
@RequestMapping("$API_URL/user-project")
class UserProjectController(
    private val userProjectService: UserProjectService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUserProject(@RequestBody @Valid dto: CreateUserProjectDTO): ReadUserProjectDTO =
        userProjectService.createUserProject(dto)

    @GetMapping("/{userLogin}/{projectId}")
    fun getUserProject(
        @PathVariable userLogin: String,
        @PathVariable projectId: Int
    ): ReadUserProjectDTO = userProjectService.getUserProject(userLogin, projectId)

    @PutMapping("/{userLogin}/{projectId}")
    fun updateUserProject(
        @PathVariable userLogin: String,
        @PathVariable projectId: Int,
        @RequestBody @Valid dto: UpdateUserProjectDTO
    ): ReadUserProjectDTO = userProjectService.updateUserProject(userLogin, projectId, dto)

    @DeleteMapping("/{userLogin}/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserProject(
        @PathVariable userLogin: String,
        @PathVariable projectId: Int
    ) {
        userProjectService.deleteUserProject(userLogin, projectId)
    }

    @GetMapping
    fun getUserProjects(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "50") size: Int
    ): Page<ReadUserProjectDTO> = userProjectService.findAll(page, size)
}