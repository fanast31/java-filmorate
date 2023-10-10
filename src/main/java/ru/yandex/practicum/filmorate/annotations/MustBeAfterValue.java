
package ru.yandex.practicum.filmorate.annotations;

import javax.validation.*;
import javax.validation.constraints.Past;
import java.lang.annotation.*;
import java.time.LocalDate;

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

class MinimumDateValidator implements ConstraintValidator<MustBeAfterValue, LocalDate> {
    private LocalDate minimumDate;

    @Override
    public void initialize(MustBeAfterValue constraintAnnotation) {
        minimumDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || value.isAfter(minimumDate);
    }
}
