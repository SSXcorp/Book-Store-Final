package mate.academy.bookstoreprod.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;
import mate.academy.bookstoreprod.dto.user.UserRegistrationRequestDto;

public class PasswordValidator implements ConstraintValidator<ValidatePassword,
        UserRegistrationRequestDto> {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[A-Z])(?=.*[,._\\-/?])(?=\\S+$).{8,40}$"
    );

    @Override
    public boolean isValid(UserRegistrationRequestDto dto, ConstraintValidatorContext context) {
        if (!PASSWORD_PATTERN.matcher(dto.getPassword()).matches()) {
            return false;
        }
        return Objects.equals(dto.getPassword(), dto.getRepeatPassword());
    }
}
