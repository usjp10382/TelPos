package com.teleios.pos.controls;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.teleios.pos.dao.utill.TeleiosPosConstant;
import com.teleios.pos.model.Supplier;
import com.teleios.pos.service.SupplierService;

@Named("suppController")
@ViewScoped
public class SupplierController implements Serializable {
	private static final long serialVersionUID = -7043427749908389157L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SupplierController.class);

	@Autowired
	private SupplierService supplierService;

	// Common Objects
	private List<Supplier> allActiveSuppliyers;
	private List<Supplier> allActiveSuppFiltered;

	// Create New Supplier Related Objects
	private Supplier newSupplier = new Supplier();

	@PostConstruct
	public void init() {
		LOGGER.info("Execute Supplier Controller Init -------->");
	}

	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		LOGGER.info("<----Global Filter Function Called----->");
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		Supplier supplier = (Supplier) value;

		return supplier.getSupplierName().toLowerCase().contains(filterText)
				|| supplier.getAddress().toLowerCase().contains(filterText);
	}

	public void createNewSuppliyer() {
		LOGGER.info("<----- Execute Create New Supplier In Controller ------>");
		if (getNewSupplier() != null) {
			try {
				if (!getNewSupplier().getEmail().isEmpty()) {
					if (!getNewSupplier().getEmail().matches(TeleiosPosConstant.EMAIL_VERIFICATION)) {
						addErrorMessageWithId("Create New Supplier", "Invalied E-mail Address Formate!");
						return;
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

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

	private void addErrorMessageWithId(String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summery, details);
		FacesContext.getCurrentInstance().addMessage("supEmail", facesMessage);
	}

	public Supplier getNewSupplier() {
		return newSupplier;
	}

	public void setNewSupplier(Supplier newSupplier) {
		this.newSupplier = newSupplier;
	}

	public List<Supplier> getAllActiveSuppliyers() {
		return allActiveSuppliyers;
	}

	public void setAllActiveSuppliyers(List<Supplier> allActiveSuppliyers) {
		this.allActiveSuppliyers = allActiveSuppliyers;
	}

	public List<Supplier> getAllActiveSuppFiltered() {
		return allActiveSuppFiltered;
	}

	public void setAllActiveSuppFiltered(List<Supplier> allActiveSuppFiltered) {
		this.allActiveSuppFiltered = allActiveSuppFiltered;
	}

}
