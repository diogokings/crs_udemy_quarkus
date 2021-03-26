package com.food.register.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.food.register.Restaurant;
import com.food.register.dto.RequestCreateRestaurantDTO;
import com.food.register.dto.RequestUpdateRestaurantDTO;
import com.food.register.dto.ResponseRestaurantDTO;

@Mapper(componentModel = "cdi")
public interface RestaurantMapper {

    public Restaurant map(RequestCreateRestaurantDTO dto);

    @Mapping(target = "createDate", dateFormat = "yyyy/MM/dd HH:mm:ss")
    @Mapping(target = "updateDate", dateFormat = "yyyy/MM/dd HH:mm:ss")
    public ResponseRestaurantDTO map(Restaurant entity);

    public Restaurant map(RequestUpdateRestaurantDTO dto, @MappingTarget Restaurant entity);
}
