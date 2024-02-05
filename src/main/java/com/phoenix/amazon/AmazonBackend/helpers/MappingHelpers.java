package com.phoenix.amazon.AmazonBackend.helpers;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;

import static com.phoenix.amazon.AmazonBackend.helpers.GenderMapHelpers.getGender;

public class MappingHelpers<U,V> {
    public static UserDto UsersToUsersDto(final Users users) {
        return new UserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .profileImage(users.getProfileImage())
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
                .primaryEmail(userDto.primaryEmail())
                .secondaryEmail(userDto.secondaryEmail())
                .password(userDto.password())
                .gender(getGender(userDto.gender()))
                .profileImage(userDto.profileImage())
                .about(userDto.about())
                .build();
    }
}
