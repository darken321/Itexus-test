package by.task.testTask.service;

import by.task.testTask.dto.UserDto;
import by.task.testTask.dto.UserSaveDto;
import by.task.testTask.model.Role;
import by.task.testTask.model.User;
import by.task.testTask.repository.RoleRepository;
import by.task.testTask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ServiceCreateIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        // Clean up the database before each test
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void testCreateUser_Success() {
        // Given
        UserSaveDto userSaveDto = UserSaveDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phones(List.of(987654321L))
                .roles(List.of("ROLE_USER"))
                .build();

        // When
        UserDto createdUser = userService.createUser(userSaveDto);

        // Then
        assertNotNull(createdUser.getId());
        assertEquals(userSaveDto.getFirstName(), createdUser.getFirstName());
        assertEquals(userSaveDto.getLastName(), createdUser.getLastName());
        assertEquals(userSaveDto.getEmail(), createdUser.getEmail());
        assertEquals(userSaveDto.getPhones(), createdUser.getPhones());
        assertEquals(userSaveDto.getRoles(), createdUser.getRoles());

        // Verify that the user is actually saved in the repository
        User userFromDb = userRepository.findById(createdUser.getId()).orElse(null);
        assertNotNull(userFromDb);
        assertEquals(userSaveDto.getFirstName(), userFromDb.getFirstName());
        assertEquals(userSaveDto.getLastName(), userFromDb.getLastName());
        assertEquals(userSaveDto.getEmail(), userFromDb.getEmail());
        assertEquals(userSaveDto.getPhones(), userFromDb.getPhones());
        assertEquals(userSaveDto.getRoles(), userFromDb.getRoles().stream().map(Role::getName).toList());
    }

    @Test
    public void testCreateUser_RoleAlreadyExists() {
        // Given
        Role existingRole = roleRepository.save(new Role("ROLE_USER"));

        UserSaveDto userSaveDto = UserSaveDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phones(List.of(987654321L))
                .roles(List.of(existingRole.getName()))
                .build();

        // When
        UserDto createdUser = userService.createUser(userSaveDto);

        // Then
        assertNotNull(createdUser.getId());
        assertEquals(userSaveDto.getFirstName(), createdUser.getFirstName());
        assertEquals(userSaveDto.getLastName(), createdUser.getLastName());
        assertEquals(userSaveDto.getEmail(), createdUser.getEmail());
        assertEquals(userSaveDto.getPhones(), createdUser.getPhones());
        assertEquals(userSaveDto.getRoles(), createdUser.getRoles());

        // Verify that the user is actually saved in the repository
        User userFromDb = userRepository.findById(createdUser.getId()).orElse(null);
        assertNotNull(userFromDb);
        assertEquals(userSaveDto.getFirstName(), userFromDb.getFirstName());
        assertEquals(userSaveDto.getLastName(), userFromDb.getLastName());
        assertEquals(userSaveDto.getEmail(), userFromDb.getEmail());
        assertEquals(userSaveDto.getPhones(), userFromDb.getPhones());
        assertEquals(userSaveDto.getRoles(), userFromDb.getRoles().stream().map(Role::getName).toList());
    }
}