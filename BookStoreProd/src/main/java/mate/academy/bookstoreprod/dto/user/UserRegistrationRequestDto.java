package mate.academy.bookstoreprod.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mate.academy.bookstoreprod.validator.ValidatePassword;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@ValidatePassword
public class UserRegistrationRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String shippingAddress;
}
