package com.food.register;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.food.register.dto.RequestCreateRestaurantDTO;
import com.food.register.dto.RequestCreateRestaurantMealDTO;
import com.food.register.dto.RequestUpdateRestaurantDTO;
import com.food.register.dto.ResponseRestaurantDTO;
import com.food.register.mapper.RestaurantMapper;
import com.food.register.mapper.RestaurantMealMapper;
import com.food.register.validation.ConstraintViolationResponse;

@Path("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Restaurant")
@RolesAllowed("owner")
@SecurityScheme(securitySchemeName = "food-auth"
        , type = SecuritySchemeType.OAUTH2
        , flows = @OAuthFlows(
                password = @OAuthFlow(tokenUrl = "http://localhost:8180/auth/realms/food/protocol/openid-connect/token")))
//@SecurityRequirement(name = "food-auth")
public class RestaurantResource {

    @Inject
    RestaurantMapper restaurantMapper;

    @Inject
    RestaurantMealMapper restaurantMealMapper;

    @GET
    public List<ResponseRestaurantDTO> listAllRestaurants() {
        Stream<Restaurant> streamRestaurants = Restaurant.streamAll();
        return streamRestaurants.map(restaurant -> restaurantMapper.map(restaurant)).collect(Collectors.toList());
    }

    @POST
    @Transactional
    @APIResponses(value = {
            @APIResponse(responseCode = "400", content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class))),
            @APIResponse(responseCode = "201", description = "Restaurante salvo com sucesso") })
    public Response createRestaurant(@Valid RequestCreateRestaurantDTO requestDTO) {
        Restaurant restaurant = restaurantMapper.map(requestDTO);
        restaurant.persist();
        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateRestaurant(@PathParam("id") Long id, RequestUpdateRestaurantDTO requestDTO) {
        Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
        if (optRestaurant.isEmpty()) {
            throw new NotFoundException("Restaurant does not exists for id=".concat(id.toString()));
        }

        Restaurant restaurant = optRestaurant.get();

        restaurantMapper.map(requestDTO, restaurant);
        restaurant.persist();
        return Response.status(Status.OK).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deleteRestaurant(@PathParam("id") Long id) {
        Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
        optRestaurant.ifPresentOrElse(Restaurant::delete, () -> {
            throw new NotFoundException("Restaurant does not exists for id=".concat(id.toString()));
        });
    }

    @GET
    @Path("{id}/meals")
    @Tag(name = "Meal")
    public List<Restaurant> listAllRestaurantMeals(@PathParam("id") Long id) {
        Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
        if (optRestaurant.isEmpty()) {
            throw new NotFoundException("Restaurant does not exists for id=".concat(id.toString()));
        }
        return RestaurantMeal.list("restaurant", optRestaurant.get());
    }

    @POST
    @Path("{id}/meals")
    @Tag(name = "Meal")
    @Transactional
    public Response createRestaurantMeal(@PathParam("id") Long id, RequestCreateRestaurantMealDTO requestDTO) {
        Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
        if (optRestaurant.isEmpty()) {
            throw new NotFoundException("Restaurant does not exists for id=".concat(id.toString()));
        }

        RestaurantMeal meal = restaurantMealMapper.map(id, requestDTO);

        meal.persist();

        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{id}/meals/{idMeal}")
    @Tag(name = "Meal")
    @Transactional
    public void updateRestaurantMeal(@PathParam("id") Long id, @PathParam("idMeal") Long idMeal,
            RequestCreateRestaurantMealDTO restaurantMealDTO) {
        Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
        if (optRestaurant.isEmpty()) {
            throw new NotFoundException("Restaurant does not exists for id=".concat(id.toString()));
        }

        Optional<RestaurantMeal> optMeal = RestaurantMeal.findByIdOptional(idMeal);
        if (optMeal.isEmpty()) {
            throw new NotFoundException("Meal does not exists for id=".concat(idMeal.toString()));
        }

        RestaurantMeal meal = optMeal.get();
        meal.price = restaurantMealDTO.price;
        meal.persist();
    }

    @DELETE
    @Path("{id}/meals/{idMeal}")
    @Tag(name = "Meal")
    @Transactional
    public void deleteRestaurantMeal(@PathParam("id") Long id, @PathParam("idMeal") Long idMeal) {
        Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
        if (optRestaurant.isEmpty()) {
            throw new NotFoundException("Restaurant does not exists for id=".concat(id.toString()));
        }

        Optional<RestaurantMeal> optMeal = RestaurantMeal.findByIdOptional(id);
        optMeal.ifPresentOrElse(RestaurantMeal::delete, () -> {
            throw new NotFoundException("Meal does not exists for id=".concat(idMeal.toString()));
        });
    }
}