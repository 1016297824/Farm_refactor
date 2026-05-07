package com.farm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
public class InitService implements InitializingBean {

    @Autowired
    private PositionService positionService;

    @Autowired
    private DiningTableService diningTableService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FertilizerService fertilizerService;

    @Override
    public void afterPropertiesSet() throws Exception {

        if (positionService.findAll().isEmpty()) {

            positionService.initPosition();
        }

        if (diningTableService.findAll().isEmpty()) {

            diningTableService.initDiningTable();
        }

        if (customerService.findAll().isEmpty()) {

            customerService.initCustomer();
        }

        if (staffService.findAll().isEmpty()) {

            staffService.intiStaff();
        }

        if (menuService.findAll().isEmpty()) {

            menuService.initMenu();
        }

        if (productService.findAll().isEmpty()) {

            productService.initProduct();
        }

        if (fertilizerService.findAll().isEmpty()) {

            fertilizerService.initFertilizer();
        }
    }
}
