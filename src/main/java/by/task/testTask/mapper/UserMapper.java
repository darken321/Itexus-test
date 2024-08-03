package by.task.testTask.mapper;


import by.task.testTask.dto.UserDto;
import by.task.testTask.dto.UserSaveDto;
import by.task.testTask.model.Phone;
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
        return modelMapper.map(dto, User.class);
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
                .phones(convertPhonesToLongs(user.getPhones()))
                .roles(convertRolesToStrings(user.getRoles()))
                .build();
    }

    private List<Long> convertPhonesToLongs(List<Phone> phones) {
        return phones.stream()
                .map(Phone::getNumber)
                .toList();
    }

    private List<String> convertRolesToStrings(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .toList();
    }
}