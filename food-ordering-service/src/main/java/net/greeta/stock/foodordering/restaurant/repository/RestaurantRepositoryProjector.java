package net.greeta.stock.foodordering.restaurant.repository;

import net.greeta.stock.common.restaurant.RestaurantAddedEvent;
import net.greeta.stock.common.restaurant.RestaurantDeletedEvent;
import net.greeta.stock.common.restaurant.RestaurantDishAddedEvent;
import net.greeta.stock.common.restaurant.RestaurantDishDeletedEvent;
import net.greeta.stock.common.restaurant.RestaurantDishUpdatedEvent;
import net.greeta.stock.common.restaurant.RestaurantUpdatedEvent;
import net.greeta.stock.foodordering.restaurant.model.Dish;
import net.greeta.stock.foodordering.restaurant.model.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantRepositoryProjector {

    private final RestaurantRepository restaurantRepository;

    @EventHandler
    public void handle(RestaurantAddedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(event.getId());
        restaurant.setName(event.getName());
        restaurantRepository.save(restaurant);
    }

    @EventHandler
    public void handle(RestaurantUpdatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getId()).ifPresent(r -> {
            r.setName(event.getName());
            restaurantRepository.save(r);
        });
    }

    @EventHandler
    public void handle(RestaurantDeletedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getId()).ifPresent(restaurantRepository::delete);
    }

    @EventHandler
    public void handle(RestaurantDishAddedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getRestaurantId()).ifPresent(r -> {
            Dish dish = new Dish();
            dish.setId(event.getDishId());
            dish.setName(event.getDishName());
            dish.setPrice(event.getDishPrice());
            r.getDishes().add(dish);
            restaurantRepository.save(r);
        });
    }

    @EventHandler
    public void handle(RestaurantDishUpdatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getRestaurantId()).ifPresent(r ->
                r.getDishes().stream().filter(d -> d.getId().equals(event.getDishId())).findAny()
                        .ifPresent(d -> {
                            d.setName(event.getDishName());
                            d.setPrice(event.getDishPrice());
                            restaurantRepository.save(r);
                        }));
    }

    @EventHandler
    public void handle(RestaurantDishDeletedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getRestaurantId()).ifPresent(r -> {
            r.getDishes().removeIf(d -> d.getId().equals(event.getDishId()));
            restaurantRepository.save(r);
        });
    }
}
