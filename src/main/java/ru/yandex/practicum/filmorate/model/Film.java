
package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.annotations.MustBeAfterValue;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Film extends BaseUnit {

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @JsonFormat
    @NotNull
    @MustBeAfterValue(value = "1895-12-28")
    private LocalDate releaseDate;

    @Min(1)
    private int duration;

    private Set<Long> likesFromUsers = new HashSet<>();

}

