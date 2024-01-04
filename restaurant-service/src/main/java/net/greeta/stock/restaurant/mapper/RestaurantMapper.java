package net.greeta.stock.restaurant.mapper;

import net.greeta.stock.restaurant.model.Dish;
import net.greeta.stock.restaurant.model.Order;
import net.greeta.stock.restaurant.model.Restaurant;
import net.greeta.stock.restaurant.rest.dto.DishResponse;
import net.greeta.stock.restaurant.rest.dto.RestaurantResponse;
import net.greeta.stock.restaurant.rest.dto.RestaurantOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantResponse toRestaurantResponse(Restaurant restaurant);

    RestaurantOrderResponse toRestaurantOrderResponse(Order order);

    @Mapping(target = "restaurantId", source = "restaurant.id")
    @Mapping(target = "dishId", source = "id")
    @Mapping(target = "dishName", source = "name")
    @Mapping(target = "dishPrice", source = "price")
    DishResponse toDishResponse(Dish dish);
}
