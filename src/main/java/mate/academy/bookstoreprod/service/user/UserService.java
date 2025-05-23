package mate.academy.bookstoreprod.service.user;

import mate.academy.bookstoreprod.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreprod.dto.user.UserResponseDto;
import mate.academy.bookstoreprod.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;
}
