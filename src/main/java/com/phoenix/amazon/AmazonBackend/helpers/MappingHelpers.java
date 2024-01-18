package com.phoenix.amazon.AmazonBackend.helpers;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;

public class MappingHelpers {
    public static UserDto UsersToUsersDto(final Users users) {
        return new UserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .email(users.getEmail())
                .gender(users.getGender())
                .imageName(users.getImageName())
                .about(users.getAbout())
                .lastSeen(users.getLastSeen())
                .build();
    }

    public static Users UserDtoToUsers(final UserDto userDto) {
        return new Users.builder()
                .userId(userDto.userId())
                .userName(userDto.userName())
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .email(userDto.email())
                .password(userDto.password())
                .gender(userDto.gender())
                .imageName(userDto.imageName())
                .about(userDto.about())
                .build();
    }
}
