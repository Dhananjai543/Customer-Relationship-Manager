package com.dhananjai.crm.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dhananjai.crm.entity.Customer;

public interface CustomerService extends JpaRepository<Customer, Integer>{
	
	public Customer findByEmail(String email);
	
//	public List<Customer> getCustomers();
//
//	public void saveCustomer(Customer theCustomer);
//	
//	public Customer getCustomer(int theId);
//
//	public void deleteCustomer(int theId);
//	
//	public Customer getCustomerByEmail(String email);
}
