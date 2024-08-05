package by.task.testTask.mapper;


import by.task.testTask.dto.user.UserDto;
import by.task.testTask.dto.user.UserSaveDto;
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
       User user = modelMapper.map(dto, User.class);
       user.setRoles(convertStringsToRoles(dto.getRoles()));
       return user;
    }

    public User fromDto(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }

    public UserDto toDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setRoles(convertRolesToStrings(user.getRoles()));
        return userDto;
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