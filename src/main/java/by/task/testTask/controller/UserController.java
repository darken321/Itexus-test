package by.task.testTask.controller;

import by.task.testTask.dto.user.UserDto;
import by.task.testTask.dto.user.UserSaveDto;
import by.task.testTask.dto.user.UserUpdateDto;
import by.task.testTask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Create
    @PostMapping
    public UserDto createUser(@RequestBody UserSaveDto userSaveDto) {
        UserDto createdUser = userService.createUser(userSaveDto);
        log.info("Created new user: {}", createdUser);
        return createdUser;
    }

    // Read All
    @GetMapping
    public List<UserDto> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        log.info("Get all users: {}", users);
        return users;
    }

    // Read
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    // Update
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable int id, @RequestBody UserUpdateDto updateDto) {
        UserDto userDto = userService.updateUser(id, updateDto);
        log.info("Update user: {}", userDto);
        return userDto;
    }

    // Delete
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Deleted user with id {}", id);
        userService.deleteUser(id);
    }
}