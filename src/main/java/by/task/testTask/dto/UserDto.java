package by.task.testTask.dto;

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
    List<String> roles = new ArrayList<>();
}
