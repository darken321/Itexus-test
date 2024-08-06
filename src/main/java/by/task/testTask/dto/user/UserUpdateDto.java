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

    @NotBlank(message = "First name is required")
    String firstName;

    String lastName;

    @NotNull(message = "Email is required")
    @NotBlank(message = "Email cannot be blank, please try again")
    @Email(message = "Email should be valid, please try again")
    @Pattern(regexp = ".+@.+\\..+", message = "Email should be valid")
    String email;

    @NotNull
    @Size(min = 1, max = 3, message = "A user must have 1 to 3 phones, please try again")
    List<String> phones = new ArrayList<>();

    @NotNull
    @Size(min = 1, max = 3,message = "A user must have 1 to 3 roles, please try again")
    List<String> roles = new ArrayList<>();
}