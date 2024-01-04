package net.greeta.stock.foodordering.customer.rest;

import net.greeta.stock.foodordering.customer.mapper.CustomerMapper;
import net.greeta.stock.foodordering.customer.rest.dto.CustomerResponse;
import net.greeta.stock.foodordering.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @GetMapping
    public List<CustomerResponse> getCustomers() {
        return customerService.getCustomers().stream().map(customerMapper::toCustomerResponse).toList();
    }
}
