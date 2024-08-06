package by.task.testTask.handler;

import by.task.testTask.dto.exception.ExceptionDto;
import by.task.testTask.mapper.ExceptionMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    ExceptionMapper dtoMapper;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto handleException(Exception exception) {
        return createExceptionDto(exception);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleNotFoundException(NoSuchElementException exception) {
        return createExceptionDto(exception);
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleExistException(EntityExistsException exception) {
        return createExceptionDto(exception);
    }

    private ExceptionDto createExceptionDto(Exception exception) {
        return dtoMapper.exceptionToDto(exception);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleValidationException(ConstraintViolationException exception) {

        return ExceptionDto.builder()
                .message(exception.getConstraintViolations().stream().toList().get(0).getMessage())
                .uuid(UUID.randomUUID())
                .type(exception.getClass().getSimpleName())
                .exceptionServerTime(ZonedDateTime.now())
                .build();
    }
}