package com.phoenix.amazon.AmazonBackend.helpers;

import com.phoenix.amazon.AmazonBackend.dto.ProductDto;
import com.phoenix.amazon.AmazonBackend.dto.UpdateUserDto;
import com.phoenix.amazon.AmazonBackend.dto.UserDto;
import com.phoenix.amazon.AmazonBackend.entity.Product;
import com.phoenix.amazon.AmazonBackend.entity.Users;

import static com.phoenix.amazon.AmazonBackend.helpers.GenderMapHelpers.getGender;

public class MappingHelpers{
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

    public static Users UserUpdateDtoToUsers(final UpdateUserDto updateUserDto) {
        return new Users.builder()
                .userId(updateUserDto.userId())
                .userName(updateUserDto.userName())
                .firstName(updateUserDto.firstName())
                .lastName(updateUserDto.lastName())
                .primaryEmail(updateUserDto.primaryEmail())
                .secondaryEmail(updateUserDto.secondaryEmail())
                .gender(getGender(updateUserDto.gender()))
                .about(updateUserDto.about())
                .build();
    }

    public static UpdateUserDto UserToUpdateUserDto(final Users users) {
        return new UpdateUserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .about(users.getAbout())
                .build();
    }

    public static Product productDtoToProduct(ProductDto productDto){
        return new Product.builder()
                .productId(productDto.productId())
                .title(productDto.title())
                .description(productDto.description())
                .price(productDto.price())
                .quantity(productDto.quantity())
                .addedDate(productDto.addedDate())
                .stock(productDto.stock())
                .build();
    }

    public static ProductDto productToProductDto(Product product){
        return new ProductDto.builder()
                .productId(product.getProductId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .addedDate(product.getAddedDate())
                .stock(product.getStock())
                .build();
    }
}
