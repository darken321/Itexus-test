package by.task.testTask.service;

import by.task.testTask.dto.UserUpdateDto;
import by.task.testTask.dto.UserDto;
import by.task.testTask.model.Role;
import by.task.testTask.model.User;
import by.task.testTask.repository.RoleRepository;
import by.task.testTask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ServiceUpdateIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Clean up the database before each test
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Create a test user
        Role role = roleRepository.save(new Role("ROLE_USER"));
        testUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phones(new ArrayList<>(List.of(123456789L)))
                .roles(new ArrayList<>(List.of(role)))
                .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    public void testUpdateUser_Success() {
        // Given
        Role newRole = roleRepository.save(new Role("ROLE_ADMIN"));
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phones(new ArrayList<>(List.of(987654321L)))
                .roles(new ArrayList<>(List.of(newRole)))
                .build();

        // When
        UserDto updatedUser = userService.updateUser(testUser.getId(), userUpdateDto);

        // Then
        assertNotNull(updatedUser);
        assertEquals(userUpdateDto.getFirstName(), updatedUser.getFirstName());
        assertEquals(userUpdateDto.getLastName(), updatedUser.getLastName());
        assertEquals(userUpdateDto.getEmail(), updatedUser.getEmail());
        assertEquals(userUpdateDto.getPhones(), updatedUser.getPhones());
        assertEquals(userUpdateDto.getRoles().stream().map(Role::getName).toList(), updatedUser.getRoles());

        // Verify that the user is actually updated in the repository
        User userFromDb = userRepository.findById(updatedUser.getId()).orElse(null);
        assertNotNull(userFromDb);
        assertEquals(userUpdateDto.getFirstName(), userFromDb.getFirstName());
        assertEquals(userUpdateDto.getLastName(), userFromDb.getLastName());
        assertEquals(userUpdateDto.getEmail(), userFromDb.getEmail());
        assertEquals(userUpdateDto.getPhones(), userFromDb.getPhones());
        assertEquals(userUpdateDto.getRoles().stream().map(Role::getName).toList(), userFromDb.getRoles().stream().map(Role::getName).toList());
    }

    @Test
    public void testUpdateUser_UserDoesNotExist() {
        // Given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phones(new ArrayList<>(List.of(987654321L)))
                .roles(new ArrayList<>(List.of(new Role("ROLE_ADMIN"))))
                .build();

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(999, userUpdateDto);
        });
        assertEquals("User with id 999 not found", exception.getMessage());
    }
}