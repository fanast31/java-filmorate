
package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.annotations.MustBeAfterValue;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
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
}

