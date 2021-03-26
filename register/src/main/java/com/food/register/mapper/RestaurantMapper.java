package com.food.register.mapper;

import org.mapstruct.Mapper;

import com.food.register.Restaurant;
import com.food.register.dto.CreateRestaurantDTO;

@Mapper(componentModel = "cdi")
public interface RestaurantMapper {

    public Restaurant map(CreateRestaurantDTO dto);
}
