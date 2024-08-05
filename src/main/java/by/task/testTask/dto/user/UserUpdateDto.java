package by.task.testTask.dto.user;

import by.task.testTask.model.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@Validated
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateDto {

    @NotEmpty(message = "First name is required")
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    String email;

    @Size(max = 3, message = "A user can have at most 3 phones")
    @NotNull
    List<Long> phones = new ArrayList<>();

    @Size(max = 3, message = "A user can have at most 3 roles")
    @NotNull
    List<Role> roles = new ArrayList<>();
}