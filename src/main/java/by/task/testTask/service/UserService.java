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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    // Create
    public UserDto createUser(UserSaveDto dto) {

        Optional<User> userWithSameEmail = userRepository.findByEmail(dto.getEmail());
        if (userWithSameEmail.isPresent()) {
            throw new EntityExistsException("Email " + dto.getEmail() + " is already in use by another user.");
        }
        List<Role> roles = dto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(roleName))))
                .toList();

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .roles(roles)
                .email(dto.getEmail())
                .phones(convertStringToPhones(dto.getPhones()))
                .build();

        return userMapper.toDto(userRepository.save(user));
    }

    private List<PhoneNumber> convertStringToPhones(List<String> phones) {
        return phones.stream().map(PhoneNumber::new).toList();

    }

    // Read All
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    // Read
    public UserDto getUserById(int id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
        return userMapper.toDto(existingUser);
    }

    // Update
    public UserDto updateUser(int id, UserUpdateDto userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));

        Optional<User> userWithSameEmail = userRepository.findByEmail(userDetails.getEmail());
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
            throw new EntityExistsException("Email " + userDetails.getEmail() + " is already in use by another user.");
        }

        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPhones(new ArrayList<>(convertStringToPhones(userDetails.getPhones())));

        List<Role> roles = userDetails.getRoles().stream()
                .map(role -> roleRepository.findByName(role)
                        .orElseGet(() -> roleRepository.save(new Role(role))))
                .toList();
        existingUser.setRoles(new ArrayList<>(roles));

        try {
            User updatedUser = userRepository.save(existingUser);
            return userMapper.toDto(updatedUser);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Database constraint violation occurred while updating user: " + ex.getMessage());
        }
    }

    // Delete
    public boolean deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        throw new NoSuchElementException("User with id " + id + " not found for deletion");
    }
}