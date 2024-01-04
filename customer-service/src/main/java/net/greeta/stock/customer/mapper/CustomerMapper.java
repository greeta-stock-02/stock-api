package net.greeta.stock.customer.mapper;

import net.greeta.stock.customer.model.Customer;
import net.greeta.stock.customer.model.Order;
import net.greeta.stock.customer.rest.dto.CustomerResponse;
import net.greeta.stock.customer.rest.dto.CustomerOrderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toCustomerResponse(Customer customer);

    CustomerOrderResponse toCustomerOrderResponse(Order order);
}
