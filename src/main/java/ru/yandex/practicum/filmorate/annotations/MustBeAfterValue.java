
package ru.yandex.practicum.filmorate.annotations;

import javax.validation.*;
import javax.validation.constraints.Past;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinimumDateValidator.class)
@Past
public @interface MustBeAfterValue {
    String message() default "Date must not be before {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    String value();
}


