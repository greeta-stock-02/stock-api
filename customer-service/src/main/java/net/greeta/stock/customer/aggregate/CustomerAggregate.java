package net.greeta.stock.customer.aggregate;

import net.greeta.stock.common.customer.CustomerAddedEvent;
import net.greeta.stock.common.customer.CustomerDeletedEvent;
import net.greeta.stock.common.customer.CustomerUpdatedEvent;
import net.greeta.stock.common.util.MyStringUtils;
import net.greeta.stock.customer.command.AddCustomerCommand;
import net.greeta.stock.customer.command.DeleteCustomerCommand;
import net.greeta.stock.customer.command.UpdateCustomerCommand;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@NoArgsConstructor
@Aggregate
public class CustomerAggregate {

    @AggregateIdentifier
    private String id;
    private String name;
    private String address;

    // -- Add Customer

    @CommandHandler
    public CustomerAggregate(AddCustomerCommand command) {
        AggregateLifecycle.apply(new CustomerAddedEvent(command.getId(), command.getName(), command.getAddress()));
    }

    @EventSourcingHandler
    public void handle(CustomerAddedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.address = event.getAddress();
    }

    // -- Update Customer

    @CommandHandler
    public void handle(UpdateCustomerCommand command) {
        String newName = MyStringUtils.getTrimmedValueOrElse(command.getName(), this.name);
        String newAddress = MyStringUtils.getTrimmedValueOrElse(command.getAddress(), this.address);
        AggregateLifecycle.apply(new CustomerUpdatedEvent(command.getId(), newName, newAddress));
    }

    @EventSourcingHandler
    public void handle(CustomerUpdatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.address = event.getAddress();
    }

    // -- Delete Customer

    @CommandHandler
    public void handle(DeleteCustomerCommand command) {
        AggregateLifecycle.apply(new CustomerDeletedEvent(command.getId()));
    }

    @EventSourcingHandler
    public void handle(CustomerDeletedEvent event) {
        AggregateLifecycle.markDeleted();
    }
}
