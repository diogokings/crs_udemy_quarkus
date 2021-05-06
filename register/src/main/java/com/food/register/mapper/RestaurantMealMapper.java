package com.food.register.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.food.register.Restaurant;
import com.food.register.RestaurantMeal;
import com.food.register.dto.RequestCreateRestaurantMealDTO;

@Mapper(componentModel = "cdi")
public interface RestaurantMealMapper {

    @Mapping(target = "restaurant", expression = "java(findById(restaurantId))")
    public RestaurantMeal map(Long restaurantId, RequestCreateRestaurantMealDTO mealDTO);

    default Restaurant findById(Long restaurantId) {
        return Restaurant.findById(restaurantId);
    }
}
