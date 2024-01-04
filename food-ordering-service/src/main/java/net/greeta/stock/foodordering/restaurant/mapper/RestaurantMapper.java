package net.greeta.stock.foodordering.restaurant.mapper;

import net.greeta.stock.foodordering.restaurant.model.Restaurant;
import net.greeta.stock.foodordering.restaurant.rest.dto.RestaurantResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantResponse toRestaurantResponse(Restaurant restaurant);
}
