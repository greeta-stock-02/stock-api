package net.greeta.stock.restaurant.repository;

import net.greeta.stock.common.order.OrderCreatedEvent;
import net.greeta.stock.common.restaurant.RestaurantAddedEvent;
import net.greeta.stock.common.restaurant.RestaurantDeletedEvent;
import net.greeta.stock.common.restaurant.RestaurantDishAddedEvent;
import net.greeta.stock.common.restaurant.RestaurantDishDeletedEvent;
import net.greeta.stock.common.restaurant.RestaurantDishUpdatedEvent;
import net.greeta.stock.common.restaurant.RestaurantUpdatedEvent;
import net.greeta.stock.restaurant.model.Dish;
import net.greeta.stock.restaurant.model.Order;
import net.greeta.stock.restaurant.model.OrderItem;
import net.greeta.stock.restaurant.model.Restaurant;
import net.greeta.stock.restaurant.query.GetRestaurantDishQuery;
import net.greeta.stock.restaurant.query.GetRestaurantOrdersQuery;
import net.greeta.stock.restaurant.query.GetRestaurantQuery;
import net.greeta.stock.restaurant.query.GetRestaurantsQuery;
import net.greeta.stock.restaurant.exception.DishNotFoundException;
import net.greeta.stock.restaurant.exception.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantRepositoryProjector {

    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;

    @QueryHandler
    public List<Restaurant> handle(GetRestaurantsQuery query) {
        return restaurantRepository.findAll();
    }

    @QueryHandler
    public Restaurant handle(GetRestaurantQuery query) {
        return restaurantRepository.findById(query.getId())
                .orElseThrow(() -> new RestaurantNotFoundException(query.getId()));
    }

    @QueryHandler
    public Dish handle(GetRestaurantDishQuery query) {
        return restaurantRepository.findById(query.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(query.getRestaurantId()))
                .getDishes()
                .stream()
                .filter(dish -> dish.getId().equals(query.getDishId()))
                .findAny()
                .orElseThrow(() -> new DishNotFoundException(query.getRestaurantId(), query.getDishId()));
    }

    @QueryHandler
    public List<Order> handle(GetRestaurantOrdersQuery query) {
        return orderRepository.findByRestaurantIdOrderByCreatedAtDesc(query.getId());
    }

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
            dish.setRestaurant(r);
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

    // -- Order Events

    @EventHandler
    public void handle(OrderCreatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        restaurantRepository.findById(event.getRestaurantId()).ifPresent(r -> {
            Order order = new Order();
            order.setId(event.getId());
            order.setCustomerName(event.getCustomerName());
            order.setCustomerAddress(event.getCustomerAddress());
            order.setStatus(event.getStatus());
            order.setTotal(event.getTotal());
            order.setCreatedAt(event.getCreatedAt());
            order.setItems(event.getItems().stream()
                    .map(i -> new OrderItem(i.getDishId(), i.getDishName(), i.getDishPrice(), i.getQuantity()))
                    .collect(Collectors.toSet()));
            order.setRestaurant(r);
            r.getOrders().add(order);
            restaurantRepository.save(r);
        });
    }
}
