package by.task.testTask.dto.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    @Positive(message = "id must be positive")
    Integer id;

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
