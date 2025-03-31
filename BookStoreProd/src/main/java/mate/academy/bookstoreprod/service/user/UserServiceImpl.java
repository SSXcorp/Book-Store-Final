package mate.academy.bookstoreprod.service.user;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreprod.dto.user.UserResponseDto;
import mate.academy.bookstoreprod.exception.RegistrationException;
import mate.academy.bookstoreprod.mapper.UserMapper;
import mate.academy.bookstoreprod.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            return userMapper.toUserResponseDto(userRepository.save(userMapper.toUser(request)));
        }
        throw new RegistrationException("Can't register user with email: " + request.getEmail());
    }
}
