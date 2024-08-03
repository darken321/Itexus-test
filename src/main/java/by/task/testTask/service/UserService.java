package by.task.testTask.service;

import by.task.testTask.dto.UserDto;
import by.task.testTask.dto.UserSaveDto;
import by.task.testTask.mapper.UserMapper;
import by.task.testTask.model.Phone;
import by.task.testTask.model.Role;
import by.task.testTask.model.User;
import by.task.testTask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    // Create
    public UserDto createUser(UserSaveDto userDto) {
        User user = userMapper.fromDto(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
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
                    existingUser.setPhones(convertLongsToPhones(userDetails.getPhones()));
                    existingUser.setRoles(convertStringsToRoles(userDetails.getRoles()));
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

    //TODO переделать
    private List<Phone> convertLongsToPhones(List<Long> phoneNumbers) {
        return phoneNumbers.stream()
                .map(number -> new Phone(0, number))
                .toList();
    }

    private List<Role> convertStringsToRoles(List<String> roles) {
        return roles.stream()
                .map(role -> new Role(0, role))
                .toList();
    }


}