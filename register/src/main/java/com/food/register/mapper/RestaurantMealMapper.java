package com.food.register.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.food.register.RestaurantMeal;
import com.food.register.dto.RequestCreateRestaurantMealDTO;

@Mapper(componentModel = "cdi")
public interface RestaurantMealMapper {

    @Mapping(target = "restaurant", source = "restaurantId")
    public RestaurantMeal map(Long restaurantId, RequestCreateRestaurantMealDTO mealDTO);

}
