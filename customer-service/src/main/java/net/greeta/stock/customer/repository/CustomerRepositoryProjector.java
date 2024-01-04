package net.greeta.stock.customer.repository;

import net.greeta.stock.common.customer.CustomerAddedEvent;
import net.greeta.stock.common.customer.CustomerDeletedEvent;
import net.greeta.stock.common.customer.CustomerUpdatedEvent;
import net.greeta.stock.common.order.OrderCreatedEvent;
import net.greeta.stock.customer.model.Customer;
import net.greeta.stock.customer.model.Order;
import net.greeta.stock.customer.model.OrderItem;
import net.greeta.stock.customer.query.GetCustomerOrdersQuery;
import net.greeta.stock.customer.query.GetCustomerQuery;
import net.greeta.stock.customer.exception.CustomerNotFoundException;
import net.greeta.stock.customer.query.GetCustomersQuery;
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
public class CustomerRepositoryProjector {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @QueryHandler
    public List<Customer> handle(GetCustomersQuery query) {
        return customerRepository.findAll();
    }

    @QueryHandler
    public Customer handle(GetCustomerQuery query) {
        return customerRepository.findById(query.getId())
                .orElseThrow(() -> new CustomerNotFoundException(query.getId()));
    }

    @QueryHandler
    public List<Order> handle(GetCustomerOrdersQuery query) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(query.getId());
    }

    @EventHandler
    public void handle(CustomerAddedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        Customer customer = new Customer();
        customer.setId(event.getId());
        customer.setName(event.getName());
        customer.setAddress(event.getAddress());
        customerRepository.save(customer);
    }

    @EventHandler
    public void handle(CustomerUpdatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        customerRepository.findById(event.getId()).ifPresent(c -> {
            c.setName(event.getName());
            c.setAddress(event.getAddress());
            customerRepository.save(c);
        });
    }

    @EventHandler
    public void handle(CustomerDeletedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        customerRepository.findById(event.getId()).ifPresent(customerRepository::delete);
    }

    // -- Order Events

    @EventHandler
    public void handle(OrderCreatedEvent event) {
        log.info("<=[E] Received an event: {}", event);
        customerRepository.findById(event.getCustomerId()).ifPresent(c -> {
            Order order = new Order();
            order.setId(event.getId());
            order.setRestaurantName(event.getRestaurantName());
            order.setStatus(event.getStatus());
            order.setTotal(event.getTotal());
            order.setCreatedAt(event.getCreatedAt());
            order.setItems(event.getItems().stream()
                    .map(i -> new OrderItem(i.getDishName(), i.getDishPrice(), i.getQuantity()))
                    .collect(Collectors.toSet()));
            order.setCustomer(c);
            c.getOrders().add(order);
            customerRepository.save(c);
        });
    }
}
