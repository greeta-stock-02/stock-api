/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.greeta.stock.order.rest;

import net.greeta.stock.order.dto.OrderCreateDto;
import net.greeta.stock.order.model.OrderStatus;
import net.greeta.stock.order.dto.OrderSummaryDto;
import net.greeta.stock.order.query.FindOrderQuery;
import net.greeta.stock.order.command.CreateOrderCommand;
import java.util.UUID;

import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersCommandController {

	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;

	@Autowired
	public OrdersCommandController(CommandGateway commandGateway, QueryGateway queryGateway) {
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
	}

	@PostMapping
	public OrderSummaryDto createOrder(@Valid @RequestBody OrderCreateDto order) {

		String orderId = UUID.randomUUID().toString();

		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
				.productId(order.getProductId()).quantity(order.getQuantity()).orderId(orderId)
				.orderStatus(OrderStatus.CREATED).build();

		SubscriptionQueryResult<OrderSummaryDto, OrderSummaryDto> queryResult = queryGateway.subscriptionQuery(
				new FindOrderQuery(orderId), ResponseTypes.instanceOf(OrderSummaryDto.class),
				ResponseTypes.instanceOf(OrderSummaryDto.class));

		try {
			commandGateway.sendAndWait(createOrderCommand);
			return queryResult.updates().blockFirst();
		} finally {
			queryResult.close();
		}

	}

}
