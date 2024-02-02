package com.phoenix.amazon.AmazonBackend.helpers;

import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.phoenix.amazon.AmazonBackend.helpers.GenderMapHelpers.getGender;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.DestinationDtoType;

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


    public static <U,V> PageableResponse<U> getPageableResponse(final Page<V> page,final DestinationDtoType destinationDtoType){
         List<V> entityList=page.getContent();
         List<U> dtoList=new ArrayList<>();
         switch (destinationDtoType){
             case USER_DTO -> {
                 if(!entityList.isEmpty() && entityList.getFirst() instanceof Users){
                     dtoList=(List<U>) entityList.stream()
                             .map(object->UsersToUsersDto((Users)object))
                             .collect(Collectors.toList());
                 }
             }
         }

         return new PageableResponse.Builder<U>()
                 .content(dtoList)
                 .pageNumber(page.getNumber()+1)
                 .pageSize(page.getSize())
                 .totalElements(page.getTotalElements())
                 .totalPages(page.getTotalPages())
                 .isLastPage(page.isLast())
                 .build();
    }
}
