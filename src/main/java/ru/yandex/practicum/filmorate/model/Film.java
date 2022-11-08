package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.service.validation.BeginOfCinemaEra;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Film {

    @PositiveOrZero
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200, message = "Очень длинное описание, максимум 200 символов.")
    private String description;
    @NotNull
    @PastOrPresent
    @BeginOfCinemaEra
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
