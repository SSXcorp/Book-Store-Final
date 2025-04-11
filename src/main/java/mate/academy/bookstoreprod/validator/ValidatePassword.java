package mate.academy.bookstoreprod.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePassword {

    String message() default "Passwords must match and meet complexity requirements.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
