package com.teleios.pos.controls;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.teleios.pos.service.CustomerService;

@Named
@ViewScoped
public class CustomerController implements Serializable{
	private static final long serialVersionUID = -1205930901856909925L;
	private static final Logger LOGGER=LoggerFactory.getLogger(CustomerController.class);
	
	//Injected OBJ
	@Autowired
	private CustomerService customerService;
	
	//Utility Object for invice
	
	@PostConstruct
	public void inti() {
		LOGGER.info("Execute Customer Controller Init --------->");
		loadAllActiveCustomers();
	}
	private void loadAllActiveCustomers() {
		LOGGER.info("<-------- Execute Load All Active Customers ------>");
	}

}
