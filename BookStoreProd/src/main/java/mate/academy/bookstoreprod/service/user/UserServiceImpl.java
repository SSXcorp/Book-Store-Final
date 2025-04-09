package mate.academy.bookstoreprod.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreprod.dto.user.UserResponseDto;
import mate.academy.bookstoreprod.exception.RegistrationException;
import mate.academy.bookstoreprod.mapper.UserMapper;
import mate.academy.bookstoreprod.model.Role;
import mate.academy.bookstoreprod.model.User;
import mate.academy.bookstoreprod.repository.role.RoleRepository;
import mate.academy.bookstoreprod.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private static final String ROLE_USER = "ROLE_USER";

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with same email already exists. Email: "
                    + request.getEmail());
        }
        User user = userMapper.toUser(request);
        Role defaultRole = roleRepository.findByName(ROLE_USER).orElseThrow();

        user.setRoles(Set.of(defaultRole));
        return userMapper.toUserResponseDto(userRepository.save(user));
    }
}
