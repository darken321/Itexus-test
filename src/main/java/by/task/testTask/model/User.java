package by.task.testTask.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "id must be positive")
    Integer id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_phones", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "number")
    @Valid
    @Size(min = 1, max = 3, message = "A user must have 1 to 3 phones, please try again")
    List<PhoneNumber> phones = new ArrayList<>();

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name is blank, please fill in")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email cannot be blank, please try again")
    @Email(message = "Email should be valid, please try again")
    @Pattern(regexp = ".+@.+\\..+", message = "Email should be valid")
    String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Size(min = 1, max = 3,message = "A user must have 1 to 3 roles, please try again")
    List<Role> roles = new ArrayList<>();

    public void addPhone(PhoneNumber phone) {
        this.phones.add(phone);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}