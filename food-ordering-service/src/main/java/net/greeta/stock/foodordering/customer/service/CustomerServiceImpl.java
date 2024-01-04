package net.greeta.stock.foodordering.customer.service;

import net.greeta.stock.foodordering.customer.repository.CustomerRepository;
import net.greeta.stock.foodordering.customer.exception.CustomerNotFoundException;
import net.greeta.stock.foodordering.customer.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer validateAndGetCustomer(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
}
