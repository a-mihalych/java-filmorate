package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {

    @PositiveOrZero
    private final int id;
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    private final String login;
    private final String name;
    @NotNull
    @PastOrPresent
    private final LocalDate birthday;
}
