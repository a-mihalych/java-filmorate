package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Genre {

    private Integer id;
    private String name;
}
