package net.greeta.stock.foodordering.order.repository;

import net.greeta.stock.foodordering.order.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
