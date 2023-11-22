package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class MPA extends BaseUnit {

    @NotBlank
    @Size(max = 100)
    private String name;

    public MPA(Long id) {
        super(id);
        name = "";
    }

    public MPA(Long id, String name) {
        super(id);
        this.name = name;
    }
}
