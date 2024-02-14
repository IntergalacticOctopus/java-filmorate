package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"id"})
@SuperBuilder
@NoArgsConstructor(force = true)
public class User {
    private Long id;
    @NotEmpty
    @Email
    private String email;
    @NotBlank
    private String login;

    private String name;
    @PastOrPresent
    private LocalDate birthday;
}
