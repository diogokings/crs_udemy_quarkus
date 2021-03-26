package com.food.register;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;
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

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.food.register.dto.CreateRestaurantDTO;
import com.food.register.mapper.RestaurantMapper;

@Path("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Restaurant")
public class RestaurantResource {

    @Inject
    RestaurantMapper restaurantMapper;

    @GET
	public List<Restaurant> listAllRestaurants() {
		return Restaurant.listAll();
	}

	@POST
	@Transactional
	public Response createRestaurant(CreateRestaurantDTO restaurantDTO) {
	    Restaurant restaurant = restaurantMapper.map(restaurantDTO);
		restaurant.persist();
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public void updateRestaurant(@PathParam("id") Long id, Restaurant dto) {
		Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
		if (optRestaurant.isEmpty()) {
			throw new NotFoundException();
		}

		Restaurant restaurant = optRestaurant.get();
		restaurant.name = dto.name;
		restaurant.persist();
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public void deleteRestaurant(@PathParam("id") Long id) {
		Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
		optRestaurant.ifPresentOrElse(Restaurant::delete, () -> {
			throw new NotFoundException();
		});
	}
	
	@GET
	@Path("{id}/dishes")
	@Tag(name = "Dish")
	public List<Restaurant> listAllRestaurantDishes(@PathParam("id") Long id) {
		Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
		if (optRestaurant.isEmpty()) {
			throw new NotFoundException("Restaurant does not exists for id=".concat(id.toString()));
		}
		return DishMenu.list("restaurant", optRestaurant.get());
	}

	@POST
	@Path("{id}/dishes")
	@Tag(name = "Dish")
	@Transactional
	public Response createRestaurantDish(@PathParam("id") Long id, DishMenu dishDTO) {
		Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
		if (optRestaurant.isEmpty()) {
			throw new NotFoundException("Restaurant does not exists for id=".concat(id.toString()));
		}
		
		DishMenu dish = new DishMenu();
		dish.name = dishDTO.name;
		dish.description = dishDTO.description;
		dish.price = dishDTO.price;
		dish.restaurant = optRestaurant.get();
		dish.persist();

		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}/dishes/{idDish}")
	@Tag(name = "Dish")
	@Transactional
	public void updateRestaurantDish(@PathParam("id") Long id, @PathParam("idDish") Long idDish, DishMenu dishDTO) {
		Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
		if (optRestaurant.isEmpty()) {
			throw new NotFoundException("Restaurant does not exists for id=".concat(id.toString()));
		}

		Optional<DishMenu> optDish = DishMenu.findByIdOptional(idDish);
		if (optDish.isEmpty()) {
			throw new NotFoundException("Dish does not exists for id=".concat(idDish.toString()));
		}
		
		DishMenu dish = optDish.get();
		dish.price = dishDTO.price;
		dish.persist();
	}

	@DELETE
	@Path("{id}")
	@Tag(name = "Dish")
	@Transactional
	public void deleteRestaurantDish(@PathParam("id") Long id, @PathParam("idDish") Long idDish) {
		Optional<Restaurant> optRestaurant = Restaurant.findByIdOptional(id);
		if (optRestaurant.isEmpty()) {
			throw new NotFoundException("Restaurant does not exists for id=".concat(id.toString()));
		}
		
		Optional<DishMenu> optDish = DishMenu.findByIdOptional(id);
		optDish.ifPresentOrElse(DishMenu::delete, () -> {
			throw new NotFoundException("Dish does not exists for id=".concat(idDish.toString()));
		});
	}
}