package ru.itmo.highload_systems_lab_1.users

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.eq
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.HttpStatus
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.web.util.DefaultUriBuilderFactory
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.itmo.highload_systems_lab_1.database.entities.User
import ru.itmo.highload_systems_lab_1.database.repositories.UserRepository
import ru.itmo.highload_systems_lab_1.dtos.CreateUserDTO
import ru.itmo.highload_systems_lab_1.dtos.ReadUserDTO
import ru.itmo.highload_systems_lab_1.utils.API_URL
import ru.itmo.highload_systems_lab_1.utils.Role
import java.util.*
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserControllerIntegrationTest {

    @LocalServerPort
    private var port: Int = 8080

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    private lateinit var userRepository: UserRepository

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:15-alpine")
    }

    @BeforeEach
    fun setUp() {
        testRestTemplate.setUriTemplateHandler(
            DefaultUriBuilderFactory("http://localhost:$port")
        )
        userRepository.deleteAll()
    }

    @Test
    fun `should create user through controller and service`() {
        val createUserDTO = CreateUserDTO(
            login = "testuser",
            password = "password123"
        )

        val response = testRestTemplate.postForEntity(
            "$API_URL/user/register",
            createUserDTO,
            ReadUserDTO::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.login).isEqualTo("testuser")

        val userOpt = userRepository.findById("testuser")
        assertThat(userOpt).isPresent
    }

    @Test
    fun `should get user by login`() {
        val user = User(
            login = "testuser",
            passwordHash = "hashed_password",
            role = Role.USER
        )
        userRepository.save(user)

        val response = testRestTemplate.getForEntity(
            "$API_URL/user/testuser",
            ReadUserDTO::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.login).isEqualTo("testuser")
    }
}
