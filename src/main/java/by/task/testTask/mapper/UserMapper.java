package by.task.testTask.mapper;


import by.task.testTask.dto.UserDto;
import by.task.testTask.dto.UserSaveDto;
import by.task.testTask.model.Role;
import by.task.testTask.model.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public User fromDto(UserSaveDto dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phones(dto.getPhones())
                .roles(convertStringsToRoles(dto.getRoles()))
                .build();
        return user;
    }

    public User fromDto(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }

    public UserDto toDto(User user) {

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phones(user.getPhones())
                .roles(convertRolesToStrings(user.getRoles()))
                .build();
    }


    private List<String> convertRolesToStrings(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .toList();
    }

    private List<Role> convertStringsToRoles(List<String> roleNames) {
        return roleNames.stream()
                .map(name -> Role.builder().name(name).build())
                .toList();
    }
}