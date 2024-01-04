/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.greeta.stock.order.command;

import net.greeta.stock.core.events.order.OrderApprovedEvent;
import net.greeta.stock.core.events.order.OrderCreatedEvent;
import net.greeta.stock.core.events.order.OrderRejectedEvent;
import net.greeta.stock.core.model.OrderStatus;
import net.greeta.stock.order.command.commands.ApproveOrderCommand;
import net.greeta.stock.order.command.commands.CreateOrderCommand;
import net.greeta.stock.order.command.commands.RejectOrderCommand;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
    
    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) throws Exception {
        this.orderId = orderCreatedEvent.getOrderId();
        this.productId = orderCreatedEvent.getProductId();
        this.userId = orderCreatedEvent.getUserId();
        this.addressId = orderCreatedEvent.getAddressId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
    }
    
    @CommandHandler
    public void handle(ApproveOrderCommand approveOrderCommand) {
    	// Create and publish the OrderApprovedEvent
    	
    	OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approveOrderCommand.getOrderId());
    	
    	AggregateLifecycle.apply(orderApprovedEvent);
    }
    
    @EventSourcingHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
    	this.orderStatus = orderApprovedEvent.getOrderStatus();
    }
 
    @CommandHandler
    public void handle(RejectOrderCommand rejectOrderCommand) {
    	
    	OrderRejectedEvent orderRejectedEvent = new OrderRejectedEvent(rejectOrderCommand.getOrderId(),
    			rejectOrderCommand.getReason());
    	
    	AggregateLifecycle.apply(orderRejectedEvent);
    	
    }
    
    @EventSourcingHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
    	this.orderStatus = orderRejectedEvent.getOrderStatus();
    }
    

}
