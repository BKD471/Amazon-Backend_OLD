package com.phoenix.amazon.AmazonBackend.helpers;

import com.phoenix.amazon.AmazonBackend.dto.PageableResponse;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UsersToUsersDto;

public class PagingHelpers {
    public static <U,V> PageableResponse<U> getPageableResponse(final Page<V> page, final AllConstantHelpers.DestinationDtoType destinationDtoType){
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
