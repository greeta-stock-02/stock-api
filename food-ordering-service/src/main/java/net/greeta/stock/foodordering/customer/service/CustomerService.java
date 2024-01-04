package net.greeta.stock.foodordering.customer.service;

import net.greeta.stock.foodordering.customer.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer validateAndGetCustomer(String id);

    List<Customer> getCustomers();
}
