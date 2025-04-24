package mate.academy.bookstoreprod.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotBlank
    @Email
    @Size(min = 6, max = 50)
    private String email;
    @NotBlank
    @Size(min = 8, max = 40)
    private String password;
}
