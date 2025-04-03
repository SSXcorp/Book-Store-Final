package mate.academy.bookstoreprod.service.user;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreprod.dto.user.UserResponseDto;
import mate.academy.bookstoreprod.exception.RegistrationException;
import mate.academy.bookstoreprod.mapper.UserMapper;
import mate.academy.bookstoreprod.model.User;
import mate.academy.bookstoreprod.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with same email already exists. Email: "
                    + request.getEmail());
        }
        User user = userMapper.toUser(request);
        return userMapper.toUserResponseDto(userRepository.save(user));
    }
}
