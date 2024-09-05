package com.dhananjai.crm.service;

import com.dhananjai.crm.entity.Order;
import com.dhananjai.crm.repository.OrderRepository;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@Service
public class ExcelDataServiceImpl implements ExcelDataService {

    @Value("${app.upload.file:${user.home}}")
    public String EXCEL_FILE_PATH;

    @Autowired
    OrderRepository orderRepository;

    Workbook workbook;

    public List<Order> getExcelDataAsList(String path) {

        List<String> list = new ArrayList<String>();

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        // Create the Workbook
        try {
            workbook = WorkbookFactory.create(new File(path));
        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();
        }

        // Retrieving the number of sheets in the Workbook
        System.out.println("-------Workbook has '" + workbook.getNumberOfSheets() + "' Sheets-----");

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Getting number of columns in the Sheet
        int noOfColumns = sheet.getRow(0).getLastCellNum();
        System.out.println("-------Sheet has '" + noOfColumns + "' columns------");

        for (Row row : sheet) {
            for (Cell cell : row) {
                System.out.println("Cell Type: " + cell.getCellType());
                if (cell.getCellType() == CellType.NUMERIC) {
                    System.out.println("Numeric Cell value: " + cell.getNumericCellValue());
                    list.add(String.valueOf(cell.getNumericCellValue()));  // Add the value as a String
                } else {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    list.add(cellValue);
                    System.out.println("Formatted Cell value: " + cellValue);
                }
            }
        }


        List<Order> orderList = createList(list, noOfColumns);

        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    private List<Order> createList(List<String> excelData, int noOfColumns) {

        ArrayList<Order> orderList = new ArrayList<Order>();

        int i = 0;
        do {
            Order order = new Order();
            order.setCustId((int)Double.parseDouble(excelData.get(i)));
            order.setProduct(excelData.get(i + 1));
            order.setReview(excelData.get(i + 2));
            System.out.println("Order uploaded: " + order.toString());

            orderList.add(order);
            i = i + (noOfColumns);

        } while (i < excelData.size());

        return orderList;
    }

    @Override
    public int saveExcelData(List<Order> orders) {
        orders = orderRepository.saveAll(orders);
        return orders.size();
    }
}