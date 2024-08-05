package by.task.testTask.service;

import by.task.testTask.dto.UserDto;
import by.task.testTask.dto.UserSaveDto;
import by.task.testTask.dto.UserUpdateDto;
import by.task.testTask.mapper.UserMapper;
import by.task.testTask.model.Role;
import by.task.testTask.model.User;
import by.task.testTask.repository.RoleRepository;
import by.task.testTask.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    // Create
    public UserDto createUser(UserSaveDto dto) {
        List<Role> roles = dto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(roleName))))
                .toList();

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .roles(roles)
                .email(dto.getEmail())
                .phones(dto.getPhones())
                .build();

        return userMapper.toDto(userRepository.save(user));
    }

    // Read All
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    // Read
    public Optional<UserDto> getUserById(int id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(userMapper::toDto);
    }

    // Update
    public UserDto updateUser(int id, UserUpdateDto userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPhones(new ArrayList<>(userDetails.getPhones())); // Ensure phones list is mutable

        List<Role> roles = userDetails.getRoles().stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseGet(() -> roleRepository.save(new Role(role.getName()))))
                .toList();
        existingUser.setRoles(new ArrayList<>(roles)); // Ensure roles list is mutable

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    // Delete
    public boolean deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}