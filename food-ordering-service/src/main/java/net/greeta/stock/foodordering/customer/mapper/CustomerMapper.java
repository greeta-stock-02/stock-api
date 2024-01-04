package net.greeta.stock.foodordering.customer.mapper;

import net.greeta.stock.foodordering.customer.model.Customer;
import net.greeta.stock.foodordering.customer.rest.dto.CustomerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toCustomerResponse(Customer customer);
}
