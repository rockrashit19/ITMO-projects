package ru.itmo.highload_systems_lab_1.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserProjectNotFoundException(userLogin: String, projectId: Int) : RuntimeException("User-project not found: user=$userLogin, project=$projectId")
