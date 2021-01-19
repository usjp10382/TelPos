package com.teleios.pos.controls;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teleios.pos.model.Customer;
import com.teleios.pos.service.CustomerService;

@Named("invController")
@ViewScoped
public class InvoiceController implements Serializable {
	private static final long serialVersionUID = -6376989656853356566L;
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);

	// Injected OBJ
	@Autowired
	private CustomerService customerService;

	// Utility Object for invoice
	private List<Customer> allActiveCustomers;

	// New Customer Details
	private String newCusFirstName;
	private String newCusLastName;
	private String newAddress;
	private String newMobile;

	// Invoice Header Objects
	private Customer selectedCustomer;

	@PostConstruct
	public void init() {
		LOGGER.info("Execute Invoice Controller Init --------->");
		loadAllActiveCustomers();
	}

	private void loadAllActiveCustomers() {
		LOGGER.info("<-------- Execute Load All Active Customers In Invoice Controller ------>");
		try {
			this.allActiveCustomers = this.customerService.getAllActiveCustomers();
			if (getAllActiveCustomers() != null) {
				this.selectedCustomer = getAllActiveCustomers().get(0);
			}
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Load All Customer Coldnt Connect to Database Server---", ste);
			addErrorMessage("Load All Active Customers", "Couldnt Connect To Database\n" + ste.getLocalizedMessage());
		} catch (EmptyResultDataAccessException ere) {
			LOGGER.error("Load All Customer Empty Customers---", ere);
			addErrorMessage("Load All Active Customers",
					"Couldnt Find Any Customers On System Admin\n" + ere.getLocalizedMessage());
		} catch (DataAccessException dae) {
			LOGGER.error("Load All CustomerInternal Server Error Please Confirm To System Admin---", dae);
			addErrorMessage("Load All Active Customers",
					"Internal Server Error Please Confirm To System Admin\n" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Load All Customer Internal Server Error Please Confirm To System Admin---", e);
			addErrorMessage("Load All Active Customers",
					"Internal Server Error Please Confirm To System Admin\n" + e.getLocalizedMessage());
		}
	}

	public List<Customer> completeCustomerContains(String query) {
		String queryLowerCase = query.toLowerCase();
		return getAllActiveCustomers().stream()
				.filter(c -> c.getFirstName().toLowerCase().contains(queryLowerCase)
						|| c.getLastName().toLowerCase().contains(queryLowerCase)
						|| c.getMobileNumber().toLowerCase().contains(queryLowerCase))
				.collect(Collectors.toList());
	}

	public void onItemSelect(SelectEvent<Customer> event) {
		LOGGER.info("Selected Customer: " + event.getObject());
		setSelectedCustomer(event.getObject());
	}

	public void createNewCustomer() {
		LOGGER.info("<----- Execute Create New Customer In Invoice Conttroller -------->");
		try {
			if (getNewCusFirstName().isEmpty()) {
				addErrorMessage("Create New Customer", "Customer First Name Is Required!");
				return;
			}
			if (getNewCusLastName().isEmpty()) {
				addErrorMessage("Create New Customer", "Customer Last Name Is Required!");
				return;
			}
			if (getNewAddress().isEmpty()) {
				addErrorMessage("Create New Customer", "Customer Address Is Required!");
				return;
			}
			if (getNewMobile().isEmpty()) {
				addErrorMessage("Create New Customer", "Customer Mobile Number Is Required!");
				return;
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = new Customer();
			if (auth != null) {
				customer.setCreateBy(auth.getName());
			} else {
				throw new AccessDeniedException("Un Authorized Access !");
			}
			customer.setFirstName(getNewCusFirstName());
			customer.setLastName(getNewCusLastName());
			customer.setAddress(getNewAddress());
			customer.setMobileNumber(getNewMobile());
			customer.setCreateDate(new Date());
			customer.setCustomerState((short) 1);
			if (this.customerService.createNewCustomer(customer) >= 1) {
				addMessage("Create New Customer", "Successfuly Create New Customer!");
				this.newAddress = null;
				this.newCusFirstName = null;
				this.newCusLastName = null;
				this.newMobile = null;
				loadAllActiveCustomers();
			} else {
				addErrorMessage("Create New Customer", "Create New Customer Failed!");
			}
		} catch (Exception e) {
			LOGGER.error("Create New Customer Errr", e);
			addErrorMessage("Create New Customer", "Create New Customer Error!\n" + e.getLocalizedMessage());
		}
	}

	// Faces Messages
	private void addMessage(String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summery, details);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	private void addWarMessage(String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, summery, details);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	private void addErrorMessage(String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summery, details);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public List<Customer> getAllActiveCustomers() {
		return allActiveCustomers;
	}

	public void setAllActiveCustomers(List<Customer> allActiveCustomers) {
		this.allActiveCustomers = allActiveCustomers;
	}

	public Customer getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(Customer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public String getNewCusFirstName() {
		return newCusFirstName;
	}

	public void setNewCusFirstName(String newCusFirstName) {
		this.newCusFirstName = newCusFirstName;
	}

	public String getNewCusLastName() {
		return newCusLastName;
	}

	public void setNewCusLastName(String newCusLastName) {
		this.newCusLastName = newCusLastName;
	}

	public String getNewAddress() {
		return newAddress;
	}

	public void setNewAddress(String newAddress) {
		this.newAddress = newAddress;
	}

	public String getNewMobile() {
		return newMobile;
	}

	public void setNewMobile(String newMobile) {
		String[] mob = newMobile.split("-");
		String correctNumber = null;

		if (mob.length > 0)
			correctNumber = newMobile;
		else
			correctNumber = mob[0] + mob[2];

		this.newMobile = correctNumber;
	}

}
