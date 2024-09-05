package com.dhananjai.crm.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dhananjai.crm.entity.Order;
import com.dhananjai.crm.service.ExcelDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dhananjai.crm.entity.Customer;
import com.dhananjai.crm.service.CustomerService;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	ExcelDataService excelservice;

	@Value("${app.upload.dir:${user.home}/Documents}")
	public String uploadDir;

	@GetMapping("/list")
	public String listCustomer(Model theModel) {
		
		//get customers from the dao
		//List<Customer> theCustomers = customerDAO.getCustomers();
		
		//get customers from the service
		List<Customer> theCustomers = customerService.findAll();
		
		//add the customers to the model
		theModel.addAttribute("customers",theCustomers);
		
		return "list-customers";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		
		//create a new model attribute to bind form data
		Customer theCustomer = new Customer();
		theModel.addAttribute("customer",theCustomer);
		
		return "customer-form";
	}
	
	@GetMapping("/showFormForDelete")
	public String showFormForDelete(@RequestParam("customerId") int theId, Model theModel) {
		
		//get the customer from the service
		Optional<Customer> theCustomer = customerService.findById(theId);
		
		//set the customer as a model attribute to pre-populate the form
		if (theCustomer.isPresent()) {
	        // Set the customer as a model attribute to pre-populate the form
	        theModel.addAttribute("customer", theCustomer.get());
	    } else {
	        // Handle the case where the customer is not found (optional)
	        theModel.addAttribute("error", "Customer not found");
	    }
		
		//send over to our form
		return "delete-form";
	}
	
	@PostMapping("/saveCustomer")
	public String saveCustomer(@ModelAttribute("customer") Customer theCustomer, Model model) {
		
		String email = theCustomer.getEmail();
		Customer existingCustomer = customerService.findByEmail(email);
		if(existingCustomer != null && !(existingCustomer.getId()==theCustomer.getId())) {
			model.addAttribute("error", "OOPS!! A user with this email already exists.");
			return "customer-form";
		}
		//save the customer using our service
		customerService.save(theCustomer);
		
		return "redirect:/customer/list";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("customerId") int theId, Model theModel) {
		
		//get the customer from the service
		Optional<Customer> theCustomer = customerService.findById(theId);
		
		//set the customer as a model attribute to pre-populate the form
		if (theCustomer.isPresent()) {
	        // Set the customer as a model attribute to pre-populate the form
	        theModel.addAttribute("customer", theCustomer.get());
	    } else {
	        // Handle the case where the customer is not found (optional)
	        theModel.addAttribute("error", "Customer not found");
	    }
		
		//send over to our form
		return "customer-form";
	}
	
	@PostMapping("/delete")
	public String deleteCustomer(@ModelAttribute("customer") Customer theCustomer) {
		
		//delete the customer
		customerService.deleteById(theCustomer.getId());
		return "redirect:/customer/list";
	}

	@GetMapping("/bulkUpload")
	public String bulkUpload() {
		return "upload-form";
	}

	@PostMapping("/uploadFile")
	public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {

		System.out.println("Uploading file ....");
		Path copyLocation = Paths
				.get(uploadDir + File.separator + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
		System.out.println(copyLocation.toString());
		List<Order> excelDataAsList = excelservice.getExcelDataAsList(copyLocation.toString());
		int noOfRecords = excelservice.saveExcelData(excelDataAsList);
		model.addAttribute("noOfRecords",noOfRecords);
		return "upload-success";
	}

}








