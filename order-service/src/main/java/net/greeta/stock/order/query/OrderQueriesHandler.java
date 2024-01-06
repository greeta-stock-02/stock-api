package net.greeta.stock.order.query;

import net.greeta.stock.order.data.OrderEntity;
import net.greeta.stock.order.data.OrdersRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import net.greeta.stock.order.dto.OrderSummaryDto;

@Component
public class OrderQueriesHandler {

	OrdersRepository ordersRepository;

	public OrderQueriesHandler(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}

	@QueryHandler
	public OrderSummaryDto findOrder(FindOrderQuery findOrderQuery) {
		OrderEntity orderEntity = ordersRepository.findByOrderId(findOrderQuery.getOrderId());
		return new OrderSummaryDto(orderEntity.getOrderId(),
				orderEntity.getOrderStatus(), "");
	}

}
