
package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseUnit {

    @Email
    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Column(nullable = false)
    @Size(max = 100)
    private String login;

    @Size(max = 100)
    private String name;

    @PastOrPresent
    @NotNull
    private LocalDate birthday;

}
