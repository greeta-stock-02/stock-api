package net.greeta.stock.foodordering.order.rest;

import net.greeta.stock.foodordering.customer.model.Customer;
import net.greeta.stock.foodordering.customer.service.CustomerService;
import net.greeta.stock.foodordering.order.command.CreateOrderCommand;
import net.greeta.stock.foodordering.order.mapper.OrderMapper;
import net.greeta.stock.foodordering.order.model.Order;
import net.greeta.stock.foodordering.order.model.OrderItem;
import net.greeta.stock.foodordering.order.query.GetOrderQuery;
import net.greeta.stock.foodordering.order.query.GetOrdersQuery;
import net.greeta.stock.foodordering.order.rest.dto.CreateOrderRequest;
import net.greeta.stock.foodordering.order.rest.dto.OrderResponse;
import net.greeta.stock.foodordering.restaurant.model.Dish;
import net.greeta.stock.foodordering.restaurant.model.Restaurant;
import net.greeta.stock.foodordering.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final OrderMapper orderMapper;

    @GetMapping
    public CompletableFuture<List<OrderResponse>> getOrders() {
        return queryGateway.query(new GetOrdersQuery(), ResponseTypes.multipleInstancesOf(Order.class))
                .thenApply(orders -> orders.stream().map(orderMapper::toOrderResponse).toList());
    }

    @GetMapping("/{id}")
    public CompletableFuture<OrderResponse> getOrder(@PathVariable UUID id) {
        return queryGateway.query(new GetOrderQuery(id.toString()), Order.class)
                .thenApply(orderMapper::toOrderResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompletableFuture<String> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Customer customer = customerService.validateAndGetCustomer(request.getCustomerId().toString());
        Restaurant restaurant = restaurantService.validateAndGetRestaurant(request.getRestaurantId().toString());
        Set<OrderItem> items = request.getItems().stream().map(i -> {
            Dish dish = restaurantService.validateAndGetRestaurantDish(restaurant.getId(), i.getDishId().toString());
            return new OrderItem(dish.getId(), dish.getName(), dish.getPrice(), i.getQuantity());
        }).collect(Collectors.toSet());

        String id = UUID.randomUUID().toString();
        return commandGateway.send(new CreateOrderCommand(id, customer.getId(), customer.getName(),
                customer.getAddress(), restaurant.getId(), restaurant.getName(), items));
    }
}
