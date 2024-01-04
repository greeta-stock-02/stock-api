package net.greeta.stock.foodordering.restaurant.rest;

import net.greeta.stock.foodordering.restaurant.mapper.RestaurantMapper;
import net.greeta.stock.foodordering.restaurant.rest.dto.RestaurantResponse;
import net.greeta.stock.foodordering.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    @GetMapping
    public List<RestaurantResponse> getRestaurants() {
        return restaurantService.getRestaurants().stream().map(restaurantMapper::toRestaurantResponse).toList();
    }
}
