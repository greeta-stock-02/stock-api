package net.greeta.stock.foodordering.restaurant.service;

import net.greeta.stock.foodordering.restaurant.exception.DishNotFoundException;
import net.greeta.stock.foodordering.restaurant.exception.RestaurantNotFoundException;
import net.greeta.stock.foodordering.restaurant.repository.RestaurantRepository;
import net.greeta.stock.foodordering.restaurant.model.Dish;
import net.greeta.stock.foodordering.restaurant.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Restaurant validateAndGetRestaurant(String id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    @Override
    public Dish validateAndGetRestaurantDish(String restaurantId, String dishId) {
        return validateAndGetRestaurant(restaurantId).getDishes().stream()
                .filter(d -> d.getId().equals(dishId)).findAny().orElseThrow(() -> new DishNotFoundException(dishId));
    }

    @Override
    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }
}
