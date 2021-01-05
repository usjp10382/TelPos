package com.teleios.pos.controls;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
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
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
	private Supplier havUpdateSuppliyer;
	private Supplier havDeleteSuppliyer;

	@PostConstruct
	public void init() {
		LOGGER.info("Execute Supplier Controller Init -------->");
		loadAllActiveSuppliyers();
	}

	private void loadAllActiveSuppliyers() {
		LOGGER.info("<---- Execute Load All Active Suppliyers In Suppliyer Controllers ------>");
		try {
			this.allActiveSuppliyers = this.supplierService.getAllActiveSuppliyer((short) 1);
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Load All Active Suppliyers Couldnt Connect to Database--> ", ste);
			addErrorMessage("Fetch All Suppliyers", "Couldnt Connect to Database\n" + ste.getLocalizedMessage());
		} catch (EmptyResultDataAccessException ere) {
			LOGGER.error("Load All Active Suppliyers Empty Suppliyer ", ere);
			addErrorMessage("Fetch All Suppliyers", "Couldnt Find Any Suppliyers\n" + ere.getLocalizedMessage());
		} catch (DataAccessException dae) {
			LOGGER.error("Load All Active Suppliyers Data access Error--> ", dae);
			addErrorMessage("Fetch All Suppliyers", "Data access Error\n" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Load All Active Suppliyers System Error--> ", e);
			addErrorMessage("Fetch All Suppliyers", "System Error\n" + e.getLocalizedMessage());
		}
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
		int saveState = 0;
		if (getNewSupplier() != null) {
			try {
				if (!getNewSupplier().getEmail().isEmpty()) {
					if (!getNewSupplier().getEmail().matches(TeleiosPosConstant.EMAIL_VERIFICATION)) {
						addErrorMessageWithId("supEmail", "Create New Supplier", "Invalied E-mail Address Formate!");
						return;
					}
				}

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null) {
					getNewSupplier().setCreateBy(auth.getName());
				} else {
					throw new AccessDeniedException("Un Authorized Access !");
				}
				getNewSupplier().setCreateDate(new Date());
				getNewSupplier().setSuppState((short) 1);

				saveState = this.supplierService.createNewSuppliyer(getNewSupplier());
				if (saveState > 0) {
					addMessage("supEmail", "Create New Suppliyer", "Suppliyer Create Success!");
					clear();
					loadAllActiveSuppliyers();
				} else {
					addErrorMessage("Create New Suppliyer", "Suppliyer Create Failed!");
				}

			} catch (SocketTimeoutException ste) {
				LOGGER.error("Create New Supplier Couldnt Connect To DB-->", ste);
				addErrorMessage("Create New Suppliyer",
						"Couldnt Reacheble To Database Server!\n" + ste.getLocalizedMessage());
			} catch (DataAccessException dae) {
				LOGGER.error("Create New Supplier Dataaccess Error-->", dae);
				addErrorMessage("Create New Suppliyer", "Dataaccess Error!\n" + dae.getLocalizedMessage());
			} catch (Exception e) {
				LOGGER.error("Create New Supplier Error--->", e);
				addErrorMessage("Create New Suppliyer",
						"Suppliyer Create Failed.. Error Ocuured!\n" + e.getLocalizedMessage());
			}
		}
	}

	public void updateSuppliyer() {
		LOGGER.info("<------- Execute Update Suppliyer In Controller -------->");
		int updateState = 0;
		try {
			if (getHavUpdateSuppliyer() != null) {

				if (!getHavUpdateSuppliyer().getEmail().isEmpty()) {
					if (!getHavUpdateSuppliyer().getEmail().matches(TeleiosPosConstant.EMAIL_VERIFICATION)) {
						addErrorMessageWithId("supEmailUp", "Update  Supplier", "Invalied E-mail Address Formate!");
						return;
					}
				}

				updateState = this.supplierService.updateSuppliyer(getHavUpdateSuppliyer());
				if (updateState > 0) {
					addMessage("supEmailUp", "Update Suppliyer", "Successfuly Update Suppliyer!");
					this.havUpdateSuppliyer = null;
					loadAllActiveSuppliyers();
				} else {
					addErrorMessageWithId("supEmailUp", "Update Suppliyer", "Suppliyer Update Failed !");
				}
			}
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Update Suppliyer Couldnt Connect To Database Server", ste);
			addErrorMessageWithId("supEmailUp", "Update Supplier", "Couldnt Connect To Database!");
		} catch (DataAccessException dae) {
			LOGGER.error("Update Suppliyer Data Access Error", dae);
			addErrorMessageWithId("supEmailUp", "Update Supplier", "Data Access Error!\n" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Update Suppliyer System Error", e);
			addErrorMessageWithId("supEmailUp", "Update Supplier",
					"Suppliyer Update Error.. Failed!\n" + e.getLocalizedMessage());
		}
	}

	public void deleteSuppliyer() {
		LOGGER.info("<------- Execute Delete Suppliyer In Controller -------->");
		int deleteState = 0;
		try {
			if (getHavDeleteSuppliyer() != null) {
				getHavDeleteSuppliyer().setSuppState((short) 0);
				deleteState = this.supplierService.deleteSuppliyer(getHavDeleteSuppliyer());
				if (deleteState > 0) {
					addMessage("Delete Suppliyer", "Successfuly Deleted Suppliyer!");
					this.havDeleteSuppliyer = null;
					loadAllActiveSuppliyers();
				} else {
					addErrorMessage("Delete Suppliyer", "Suppliyer Delete Failed !");
				}
			}
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Delete Suppliyer Couldnt Connect To Database Server", ste);
			addErrorMessage("Delete Supplier", "Couldnt Connect To Database!");
		} catch (DataAccessException dae) {
			LOGGER.error("Delete Suppliyer Data Access Error", dae);
			addErrorMessage("Delete Supplier", "Data Access Error!\n" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Delete Suppliyer System Error", e);
			addErrorMessage("Delete Supplier", "Suppliyer Delete Error.. Failed!\n" + e.getLocalizedMessage());
		}
	}

	private void addMessage(String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summery, details);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	private void addMessage(String clinId, String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summery, details);
		FacesContext.getCurrentInstance().addMessage(clinId, facesMessage);
	}

	private void addErrorMessage(String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summery, details);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	private void addErrorMessageWithId(String clientId, String summery, String details) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summery, details);
		FacesContext.getCurrentInstance().addMessage(clientId, facesMessage);
	}

	private void clear() {
		LOGGER.info("Execute Clear in Supplier Controller ------->");
		try {
			this.newSupplier = null;
			this.newSupplier = new Supplier();
		} catch (Exception e) {
			LOGGER.error("Clear input Error-->", e);
			addErrorMessage("Create New Suppliyer", "Clear input Error Ocuured!\n" + e.getLocalizedMessage());
		}
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

	public Supplier getHavUpdateSuppliyer() {
		return havUpdateSuppliyer;
	}

	public void setHavUpdateSuppliyer(Supplier havUpdateSuppliyer) {
		this.havUpdateSuppliyer = havUpdateSuppliyer;
	}

	public Supplier getHavDeleteSuppliyer() {
		return havDeleteSuppliyer;
	}

	public void setHavDeleteSuppliyer(Supplier havDeleteSuppliyer) {
		this.havDeleteSuppliyer = havDeleteSuppliyer;
		deleteSuppliyer();
	}

}
