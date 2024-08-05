package by.task.testTask.controller;

import by.task.testTask.dto.UserDto;
import by.task.testTask.dto.UserSaveDto;
import by.task.testTask.dto.UserUpdateDto;
import by.task.testTask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserSaveDto userSaveDto) {
        UserDto createdUser = userService.createUser(userSaveDto);
        log.info("Created new user: {}", createdUser);
        return ResponseEntity.ok(createdUser);
    }

    // Read All
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        log.info("Get all users: {}", users);
        return ResponseEntity.ok(users);
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable int id) {
        Optional<UserDto> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            UserDto user = userOpt.get();
            log.info("Get user: {}", user);
            return ResponseEntity.ok(user);
        } else {
            log.warn("User with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable int id, @RequestBody UserUpdateDto userDto) {
        try {
            UserDto updatedUser = userService.updateUser(id, userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            log.info("Deleted user with id {}", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("User with id {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}