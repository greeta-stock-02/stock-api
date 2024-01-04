package net.greeta.stock.foodordering.restaurant.service;

import net.greeta.stock.foodordering.restaurant.model.Dish;
import net.greeta.stock.foodordering.restaurant.model.Restaurant;

import java.util.List;

public interface RestaurantService {

    Restaurant validateAndGetRestaurant(String id);

    Dish validateAndGetRestaurantDish(String restaurantId, String dishId);

    List<Restaurant> getRestaurants();
}
