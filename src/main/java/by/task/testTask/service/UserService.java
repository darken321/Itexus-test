package by.task.testTask.service;

import by.task.testTask.dto.UserDto;
import by.task.testTask.dto.UserSaveDto;
import by.task.testTask.mapper.UserMapper;
import by.task.testTask.model.Role;
import by.task.testTask.model.User;
import by.task.testTask.repository.RoleRepository;
import by.task.testTask.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


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
    //TODO переделать
    public Optional<UserDto> updateUser(int id, UserDto userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(userDetails.getFirstName());
                    existingUser.setLastName(userDetails.getLastName());
                    existingUser.setEmail(userDetails.getEmail());
//                    existingUser.setPhones(convertLongsToPhones(userDetails.getPhones()));
//                    existingUser.setRoles(convertStringsToRoles(userDetails.getRoles()));
                    User updatedUser = userRepository.save(existingUser);
                    return userMapper.toDto(updatedUser);
                });
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