package by.task.testTask.service;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ServiceGetIntegrationTest {

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
                .phones(List.of(123456789L))
                .roles(List.of(role))
                .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    public void testGetUserById_UserExists() {
        Optional<UserDto> userDtoOpt = userService.getUserById(testUser.getId());
        assertTrue(userDtoOpt.isPresent());

        UserDto userDto = userDtoOpt.get();
        assertEquals(testUser.getId(), userDto.getId());
        assertEquals(testUser.getFirstName(), userDto.getFirstName());
        assertEquals(testUser.getLastName(), userDto.getLastName());
        assertEquals(testUser.getEmail(), userDto.getEmail());
        assertEquals(new ArrayList<>(testUser.getPhones()), userDto.getPhones());
        assertEquals(testUser.getRoles().stream().map(Role::getName).toList(), userDto.getRoles());
    }

    @Test
    public void testGetUserById_UserDoesNotExist() {
        Optional<UserDto> userDtoOpt = userService.getUserById(999);
        assertFalse(userDtoOpt.isPresent());
    }
}