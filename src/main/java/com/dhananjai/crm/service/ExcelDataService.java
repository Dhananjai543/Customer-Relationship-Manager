package com.dhananjai.crm.service;

import com.dhananjai.crm.entity.Customer;
import com.dhananjai.crm.entity.Order;

import java.util.List;

public interface ExcelDataService {

    List<Order> getExcelDataAsList(String path);
    int saveExcelData(List<Order> orders);


}
