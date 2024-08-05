package by.task.testTask.dto.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;
import java.util.UUID;

@ToString
@Getter
@Builder
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExceptionDto {
    String message;
    UUID uuid;
    ZonedDateTime exceptionServerTime;
    String type;
}