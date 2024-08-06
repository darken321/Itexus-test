package by.task.testTask.service;

import by.task.testTask.dto.user.UserDto;
import by.task.testTask.dto.user.UserSaveDto;
import by.task.testTask.dto.user.UserUpdateDto;
import by.task.testTask.mapper.UserMapper;
import by.task.testTask.model.PhoneNumber;
import by.task.testTask.model.Role;
import by.task.testTask.model.User;
import by.task.testTask.repository.RoleRepository;
import by.task.testTask.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class IntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;


    private User testUser;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = userService.getOrCreateRole("ROLE_USER");

        testUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phones(List.of(new PhoneNumber("375123456789")))
                .roles(List.of(role))
                .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    public void testCreateUser() {
        UserSaveDto userSaveDto = UserSaveDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phones(List.of("375987654321"))
                .roles(List.of("ROLE_USER"))
                .build();

        UserDto createdUser = userService.createUser(userSaveDto);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getFirstName()).isEqualTo("Jane");
        assertThat(createdUser.getLastName()).isEqualTo("Doe");
        assertThat(createdUser.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(createdUser.getPhones()).containsExactly("375987654321");
        assertThat(createdUser.getRoles()).containsExactly("ROLE_USER");
    }

    @Test
    public void testCreateUserEmailAlreadyExists() {
        UserSaveDto secondUserSaveDto = UserSaveDto.builder()
                .firstName("John")
                .lastName("Smith")
                .email("john.doe@example.com")
                .phones(List.of("375123456789"))
                .roles(List.of("ROLE_ADMIN"))
                .build();

        EntityExistsException exception = assertThrows(EntityExistsException.class, () -> {
            userService.createUser(secondUserSaveDto);
        });

        assertThat(exception.getMessage())
                .isEqualTo("Email john.doe@example.com is already in use by another user.");
    }

    @Test
    public void testGetUserByIdUserExists() {
        UserDto userDto = userService.getUserById(testUser.getId());

        assertThat(userDto).usingRecursiveComparison().isEqualTo(userMapper.toDto(testUser));
    }

    @Test
    public void testGetUserByIdUserDoesNotExist() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            userService.getUserById(999);
        });

        assertThat(exception.getMessage()).isEqualTo("User with id 999 not found");
    }

    @Test
    public void testUpdateUserExists() {
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .firstName("Johnny")
                .lastName("Doe")
                .email("johnny.doe@example.com")
                .phones(List.of("375987654321"))
                .roles(List.of("ROLE_ADMIN"))
                .build();

        UserDto expectedUserDto = UserDto.builder()
                .id(testUser.getId())
                .firstName("Johnny")
                .lastName("Doe")
                .email("johnny.doe@example.com")
                .phones(List.of("375987654321"))
                .roles(List.of("ROLE_ADMIN"))
                .build();

        UserDto updatedUser = userService.updateUser(testUser.getId(), userUpdateDto);

        assertThat(updatedUser).usingRecursiveComparison().isEqualTo(expectedUserDto);
    }

    @Test
    public void testUpdateUserDoesNotExist() {
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .firstName("Johnny")
                .lastName("Doe")
                .email("johnny.doe@example.com")
                .phones(List.of("375987654321"))
                .roles(List.of("ROLE_ADMIN"))
                .build();

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            userService.updateUser(999, userUpdateDto);
        });

        assertThat(exception.getMessage()).isEqualTo("User with id 999 not found");
    }

    @Test
    public void testDeleteUserUserExists() {
        boolean isDeleted = userService.deleteUser(testUser.getId());
        assertThat(isDeleted).isTrue();
        assertThat(userRepository.existsById(testUser.getId())).isFalse();
    }

    @Test
    public void testDeleteUserUserDoesNotExist() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            userService.deleteUser(999);
        });

        assertThat(exception.getMessage()).isEqualTo("User with id 999 not found for deletion");
    }

    @Test
    public void testCreateUserWithMoreThanThreePhones() {
        UserSaveDto userSaveDto = UserSaveDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phones(List.of("375987654321", "375987654322", "375987654323", "375987654324"))
                .roles(List.of("ROLE_USER"))
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            userService.createUser(userSaveDto);
        });

        assertThat(exception.getMessage()).contains("A user must have 1 to 3 phones, please try again");
    }

    @Test
    public void testCreateUserWithMoreThanThreeRoles() {
        UserSaveDto userSaveDto = UserSaveDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phones(List.of("375987654321"))
                .roles(List.of("ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SUPERVISOR"))
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            userService.createUser(userSaveDto);
        });

        assertThat(exception.getMessage()).contains("A user must have 1 to 3 roles, please try again");
    }

    @Test
    public void testCreateUserWithPhoneNumberHaving11Digits() {
        UserSaveDto userSaveDto = UserSaveDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phones(List.of("37512345678")) // 11 digits
                .roles(List.of("ROLE_USER"))
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            userService.createUser(userSaveDto);
        });

        assertThat(exception.getMessage()).contains("Phone number must match the format 375*********");
    }

    @Test
    public void testCreateUserWithPhoneNumberHaving13Digits() {
        UserSaveDto userSaveDto = UserSaveDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phones(List.of("3751234567890")) // 13 digits
                .roles(List.of("ROLE_USER"))
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            userService.createUser(userSaveDto);
        });

        assertThat(exception.getMessage()).contains("Phone number must match the format 375*********");
    }

}
