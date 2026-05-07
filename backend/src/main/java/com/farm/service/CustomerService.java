package com.farm.service;

import com.farm.entity.Customer;
import com.farm.repository.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Customer> findAll() {

        return customerMapper.findAll();
    }

    public Customer findByUsername(String name) {

        return customerMapper.findByUsername(name);
    }

    public void addCustomer(Customer customer) {

        customerMapper.insert(customer);
    }

    public void updateCustomer(Customer customer){

        customerMapper.updateByPrimaryKey(customer);
    }

    public void initCustomer() {

        List<Customer> customerList = new ArrayList<Customer>();

        for (int i = 0; i < 3; i++) {
            Customer customer = new Customer("1554663266" + (i + 1), passwordEncoder.encode("123456"), "zk");
            customerList.add(customer);
        }

        for (Customer customer : customerList) {
            customerMapper.insert(customer);
        }
    }
}
