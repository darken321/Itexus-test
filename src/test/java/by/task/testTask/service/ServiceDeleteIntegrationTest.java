package by.task.testTask.service;

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
public class ServiceDeleteIntegrationTest {

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
    public void testDeleteUser_UserExists() {
        boolean isDeleted = userService.deleteUser(testUser.getId());
        assertTrue(isDeleted);
        assertFalse(userRepository.existsById(testUser.getId()));
    }

    @Test
    public void testDeleteUser_UserDoesNotExist() {
        boolean isDeleted = userService.deleteUser(999);
        assertFalse(isDeleted);
    }
}