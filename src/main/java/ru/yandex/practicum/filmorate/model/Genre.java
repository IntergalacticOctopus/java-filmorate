package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
public class Genre {
    @NotNull
    private Long id;
    @NotNull
    private String name;
}