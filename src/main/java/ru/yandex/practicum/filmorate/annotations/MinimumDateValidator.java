package ru.yandex.practicum.filmorate.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

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
