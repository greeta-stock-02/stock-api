package net.greeta.stock.order.saga;

import net.greeta.stock.order.command.ApproveOrderCommand;
import net.greeta.stock.product.commands.ReserveProductCommand;
import net.greeta.stock.order.events.OrderApprovedEvent;
import net.greeta.stock.order.events.OrderCreatedEvent;
import net.greeta.stock.order.events.OrderRejectedEvent;
import net.greeta.stock.order.events.ProductReservedEvent;
import net.greeta.stock.order.command.RejectOrderCommand;
import net.greeta.stock.order.dto.OrderSummaryDto;
import net.greeta.stock.order.query.FindOrderQuery;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
public class OrderSaga {
	
	@Autowired
	private transient CommandGateway commandGateway;
	
	@Autowired
	private transient QueryGateway queryGateway;
	
	@Autowired
	private transient DeadlineManager deadlineManager;
	
	@Autowired
	private transient QueryUpdateEmitter queryUpdateEmitter;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderSaga.class);
	
	@StartSaga
	@SagaEventHandler(associationProperty="orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {

		ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
				.orderId(orderCreatedEvent.getOrderId())
				.productId(orderCreatedEvent.getProductId())
				.quantity(orderCreatedEvent.getQuantity())
				.build();
		
		LOGGER.info("OrderCreatedEvent handled for orderId: " + reserveProductCommand.getOrderId() + 
				" and productId: " + reserveProductCommand.getProductId() );
		
		commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {

			@Override
			public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
					CommandResultMessage<? extends Object> commandResultMessage) {
				   if(commandResultMessage.isExceptional()) {
					   // Start a compensating transaction
						RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(orderCreatedEvent.getOrderId(),
								commandResultMessage.exceptionResult().getMessage());
						
						commandGateway.send(rejectOrderCommand);
				   }
				
			}
			
		});
		
 
	}
	
	@SagaEventHandler(associationProperty="orderId")
	public void handle(ProductReservedEvent productReservedEvent) {
		// TODO: Process user payment
        LOGGER.info("ProductReservedEvent is called for productId: "+ productReservedEvent.getProductId() +
        		" and orderId: " + productReservedEvent.getOrderId());

		// Send an ApproveOrderCommand
		ApproveOrderCommand approveOrderCommand =
				new ApproveOrderCommand(productReservedEvent.getOrderId());

		commandGateway.send(approveOrderCommand);
 
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty="orderId")
	public void handle(OrderApprovedEvent orderApprovedEvent) {
		LOGGER.info("Order is approved. Order Saga is complete for orderId: " + orderApprovedEvent.getOrderId());
	    //SagaLifecycle.end();
		queryUpdateEmitter.emit(FindOrderQuery.class, query -> true,
				new OrderSummaryDto(orderApprovedEvent.getOrderId(),
						orderApprovedEvent.getOrderStatus(),
						""));
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty="orderId")
	public void handle(OrderRejectedEvent orderRejectedEvent) {
		LOGGER.info("Successfully rejected order with id " + orderRejectedEvent.getOrderId());
		
		queryUpdateEmitter.emit(FindOrderQuery.class, query -> true, 
				new OrderSummaryDto(orderRejectedEvent.getOrderId(),
						orderRejectedEvent.getOrderStatus(),
						orderRejectedEvent.getReason()));
	}
	
}
