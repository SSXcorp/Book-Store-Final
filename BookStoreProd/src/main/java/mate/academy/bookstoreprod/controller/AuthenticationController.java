package mate.academy.bookstoreprod.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreprod.dto.user.UserResponseDto;
import mate.academy.bookstoreprod.exception.RegistrationException;
import mate.academy.bookstoreprod.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users management",
        description = "Endpoints for managing authentication and authorization")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/auth/registration")
    @Operation(summary = "Register new user",
            description = "Register new user and returns it")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
