package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.exception.validation.BeginOfCinemaEra;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Setter
@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    @PositiveOrZero
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200, message = "Очень длинное описание, максимум 200 символов.")
    private String description;
    @NotNull
    @PastOrPresent
    @BeginOfCinemaEra
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Integer duration;
}
