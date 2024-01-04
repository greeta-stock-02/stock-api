package net.greeta.stock.customer.repository;

import net.greeta.stock.customer.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByCustomerIdOrderByCreatedAtDesc(String customerId);
}
