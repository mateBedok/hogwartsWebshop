package com.codecool.shop.dao;

import com.codecool.shop.model.Customer;
import com.codecool.shop.model.Product;

import java.util.List;

/**
 * Data access object interface to maintain customers in the database.
 * @since 1.0
 */
public interface CustomerDao {
    void add(Customer customer);
    void update(Customer customer);
    Customer find(int id);
    Customer getCustomerByEmail(String email);
    public String getActualCustomerName(Integer id);

    List<Customer> getCustomers();
}