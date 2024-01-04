package net.greeta.stock.order.query;

import net.greeta.stock.order.core.data.OrderEntity;
import net.greeta.stock.order.core.data.OrdersRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import net.greeta.stock.order.core.model.OrderSummary;

@Component
public class OrderQueriesHandler {

	OrdersRepository ordersRepository;

	public OrderQueriesHandler(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}

	@QueryHandler
	public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
		OrderEntity orderEntity = ordersRepository.findByOrderId(findOrderQuery.getOrderId());
		return new OrderSummary(orderEntity.getOrderId(), 
				orderEntity.getOrderStatus(), "");
	}

}
