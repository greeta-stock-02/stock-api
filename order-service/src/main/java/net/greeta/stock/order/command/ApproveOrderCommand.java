package net.greeta.stock.order.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApproveOrderCommand {

	@TargetAggregateIdentifier
	private final String orderId;
}
