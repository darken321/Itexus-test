package by.task.testTask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @Column(name = "phone_number")
    @Size(max = 3, message = "A user can have at most 3 phones")
    List<Long> phones = new ArrayList<>();

    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "First name is required")
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Size(max = 3, message = "A user can have at most 3 roles")
    List<Role> roles = new ArrayList<>();

    public void addPhone(Long phone) {
        this.phones.add(phone);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}