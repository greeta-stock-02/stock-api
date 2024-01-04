package net.greeta.stock.order.core.model;

import lombok.Value;
import net.greeta.stock.core.model.OrderStatus;

@Value
public class OrderSummary {

	private final String orderId;
	private final OrderStatus orderStatus;
	private final String message;
	
}
