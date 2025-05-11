package mate.academy.bookstoreprod.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreprod.dto.user.UserResponseDto;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.exception.RegistrationException;
import mate.academy.bookstoreprod.mapper.UserMapper;
import mate.academy.bookstoreprod.model.Role;
import mate.academy.bookstoreprod.model.RoleName;
import mate.academy.bookstoreprod.model.User;
import mate.academy.bookstoreprod.repository.role.RoleRepository;
import mate.academy.bookstoreprod.repository.user.UserRepository;
import mate.academy.bookstoreprod.service.shoppingcart.ShoppingCartService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with same email already exists. Email: "
                    + request.getEmail());
        }

        User user = userMapper.toUser(request);
        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Given role is not present in a database: " + RoleName.ROLE_USER.name())
                );
        user.setRoles(Set.of(defaultRole));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        shoppingCartService.createShoppingCart(user);
        return userMapper.toUserResponseDto(user);
    }
}
