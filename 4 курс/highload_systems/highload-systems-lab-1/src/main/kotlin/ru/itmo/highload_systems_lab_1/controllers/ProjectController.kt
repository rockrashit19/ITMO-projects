package ru.itmo.highload_systems_lab_1.controllers

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.itmo.highload_systems_lab_1.dtos.CreateProjectDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadProjectDTO
import ru.itmo.highload_systems_lab_1.dtos.UpdateProjectDTO
import ru.itmo.highload_systems_lab_1.services.ProjectService
import ru.itmo.highload_systems_lab_1.utils.API_URL

@RestController
@RequestMapping("$API_URL/project")
class ProjectController(
    private val projectService: ProjectService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProject(@RequestBody @Valid dto: CreateProjectDTO): ReadProjectDTO =
        projectService.createProject(dto)

    @GetMapping("/{id}")
    fun getProject(@PathVariable id: Int): ReadProjectDTO =
        projectService.getProject(id)

    @PutMapping("/{id}")
    fun updateProject(
        @PathVariable id: Int,
        @RequestBody @Valid dto: UpdateProjectDTO
    ): ReadProjectDTO = projectService.updateProject(id, dto)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProject(@PathVariable id: Int) {
        projectService.deleteProject(id)
    }

    @GetMapping
    fun getProjects(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "50") size: Int
    ): Page<ReadProjectDTO> = projectService.findAll(page, size)

    @GetMapping("/infinite")
    fun getProjectsInfinite(
        @RequestParam(required = false) cursor: Int?,
        @RequestParam(defaultValue = "20") size: Int
    ): List<ReadProjectDTO> = projectService.findAllInfinite(cursor, size)

    @GetMapping("/paginated")
    fun getProjectsPaginated(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "50") size: Int
    ): ResponseEntity<Page<ReadProjectDTO>> {
        val result = projectService.findAll(page, size)
        val headers = HttpHeaders()
        headers.add("X-Total-Count", result.totalElements.toString())
        return ResponseEntity.ok().headers(headers).body(result)
    }
}
