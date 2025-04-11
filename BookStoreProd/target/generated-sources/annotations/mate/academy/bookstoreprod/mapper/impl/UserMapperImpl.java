package mate.academy.bookstoreprod.mapper.impl;

import javax.annotation.processing.Generated;
import mate.academy.bookstoreprod.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreprod.dto.user.UserResponseDto;
import mate.academy.bookstoreprod.mapper.UserMapper;
import mate.academy.bookstoreprod.model.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-11T10:59:06+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (JetBrains s.r.o.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDto toUserResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        if ( user.getId() != null ) {
            userResponseDto.setId( user.getId() );
        }
        if ( user.getEmail() != null ) {
            userResponseDto.setEmail( user.getEmail() );
        }
        if ( user.getFirstName() != null ) {
            userResponseDto.setFirstName( user.getFirstName() );
        }
        if ( user.getLastName() != null ) {
            userResponseDto.setLastName( user.getLastName() );
        }
        if ( user.getShippingAddress() != null ) {
            userResponseDto.setShippingAddress( user.getShippingAddress() );
        }

        return userResponseDto;
    }

    @Override
    public User toUser(UserRegistrationRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        User user = new User();

        if ( requestDto.getEmail() != null ) {
            user.setEmail( requestDto.getEmail() );
        }
        if ( requestDto.getPassword() != null ) {
            user.setPassword( requestDto.getPassword() );
        }
        if ( requestDto.getFirstName() != null ) {
            user.setFirstName( requestDto.getFirstName() );
        }
        if ( requestDto.getLastName() != null ) {
            user.setLastName( requestDto.getLastName() );
        }
        if ( requestDto.getShippingAddress() != null ) {
            user.setShippingAddress( requestDto.getShippingAddress() );
        }

        return user;
    }
}
