package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
public class Mpa {
    @NotNull
    private Long id;

    @NotNull
    private String name;
}