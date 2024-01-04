package net.greeta.stock.foodordering.restaurant.rest.dto;

import java.math.BigDecimal;
import java.util.Set;

public record RestaurantResponse(String id, String name, Set<Dish> dishes) {

    public record Dish(String id, String name, BigDecimal price) {
    }
}
