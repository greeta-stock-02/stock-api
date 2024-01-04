package net.greeta.stock.foodordering.restaurant.repository;

import net.greeta.stock.foodordering.restaurant.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
}
