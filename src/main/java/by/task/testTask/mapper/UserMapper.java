package by.task.testTask.mapper;


import by.task.testTask.dto.user.UserDto;
import by.task.testTask.dto.user.UserSaveDto;
import by.task.testTask.model.PhoneNumber;
import by.task.testTask.model.Role;
import by.task.testTask.model.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public User fromDto(UserSaveDto dto) {
        User user = modelMapper.map(dto, User.class);
        user.setRoles(dto.getRoles().stream()
                .map(name -> Role.builder().name(name).build())
                .toList());
        return user;
    }

    public User fromDto(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }

    public UserDto toDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .toList());
        userDto.setPhones(user.getPhones().stream()
                .map(PhoneNumber::getNumber)
                .toList());
        return userDto;
    }
}