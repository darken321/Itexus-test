package by.task.testTask.config;

import by.task.testTask.mapper.UserMapper;
import by.task.testTask.model.PhoneNumber;
import by.task.testTask.model.Role;
import by.task.testTask.model.User;
import by.task.testTask.repository.RoleRepository;
import by.task.testTask.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopulateDB {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;

//    @PostConstruct
    public void init() {

        //нашел роль в БД
        Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        //собрал юзера с ролями
        User user = User.builder()
                .firstName("Илон")
                .lastName("Маск")
                .roles(roles)
                .email("elon@mask.com")
                .build();

        //добавил к нему телефоны
        user.addPhone(new PhoneNumber("175000236589"));
        user.addPhone(new PhoneNumber("175000236590"));
        user.addPhone(new PhoneNumber("175000236591"));
        //сохранил юзера
        userRepository.save(user);
    }
}