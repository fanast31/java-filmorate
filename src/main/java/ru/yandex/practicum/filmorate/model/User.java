
package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
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
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String login;

    private String name;

    @PastOrPresent
    @NotNull
    private LocalDate birthday;

}
