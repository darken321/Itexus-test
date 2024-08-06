package by.task.testTask.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumber {

    @NotBlank(message = "Phone number is blank, please fill in")
    @Pattern(regexp = "375\\d{9}", message = "Phone number must match the format 375*********")
    private String number;
}