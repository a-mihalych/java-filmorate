package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {

    @PositiveOrZero
    private final int id;
    @NotBlank
    private final String name;
    @NotBlank
    @Size(max = 200, message = "Очень длинное описание, максимум 200 символов.")
    private final String description;
    @NotNull
    @PastOrPresent
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
}
