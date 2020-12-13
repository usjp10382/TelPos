package com.teleios.pos.controls;

import java.io.Serializable;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teleios.pos.model.Uom;
import com.teleios.pos.service.UomService;

@Named("uomController")
@ViewScoped
public class UomController implements Serializable {

	/**
	 * @author Harith Ahangama
	 * @Date 2020.12.12
	 * @Discription Manage bean for control Uom
	 */
	private static final long serialVersionUID = -6771059177352287972L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UomController.class);

	@Autowired
	private UomService uomService;

	private Uom selectedUom = new Uom();
	private Uom deleteUom = new Uom();
	private List<Uom> uomList;
	private List<Uom> filteredUom;

	@PostConstruct
	public void init() {
		LOGGER.info("<-----------Execute UomController Init------------->");
		loadAllUom();
	}

	private void loadAllUom() {
		try {
			getSelectedUom().setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			getSelectedUom().setCreateDate(new Date());
			this.uomList = uomService.getActiveUoms();
		} catch (EmptyResultDataAccessException empe) {
			LOGGER.error("Loead All Uom Is Emplty", empe);
			addWarMessage("Unit Of Mesherment Init", "Doesnt Contains Any UOM");
		} catch (DataAccessException dae) {
			LOGGER.error("Loead All Uom Is Daa Acc Error", dae);
			addErrorMessage("Unit Of Mesherment Init", "Data AccesError Ocured-->" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Unexpected Error---", e);
			addErrorMessage("Unit Of Mesherment Init", "System Error Ocured-->" + e.getLocalizedMessage());
		}
	}

	// Filter Function For Unit Of measurement Table
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		LOGGER.info("<----Global Filter Function Called----->");
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		Uom uom = (Uom) value;

		return uom.getUomName().toLowerCase().contains(filterText)
				|| uom.getUomChar().toLowerCase().contains(filterText);
	}

	// UOM Save Function
	public void creaeNewUom() {
		LOGGER.info("Execute Create New Uom--------->");
		int saveState = 0;
		try {
			getSelectedUom().setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			getSelectedUom().setCreateDate(new Date());
			saveState = this.uomService.createNewUom(getSelectedUom());
			if (saveState > 0) {
				LOGGER.info("<--------- Create New UOM Success--------->");
				addMessage("Create New UOM", "Create New UOM Success!");
				loadAllUom();
				clearFiled();
			} else {
				addErrorMessage("Create New UOM", "Create New UOM Falied");
			}

		} catch (DuplicateKeyException de) {
			LOGGER.error("Create New UOM Name/Charactor Duplicate Ocurr----------> ", de);
			addErrorMessage("Create New UOM",
					"Entered UOM Name Or Charactor Allredy Exit\n" + de.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Create New UOM Error Ocurr----------> ", e);
			addErrorMessage("Create New UOM", "Create New UOM Falied\n" + e.getLocalizedMessage());
		}
	}

	// UOM Update Function
	public void updateUom() {
		LOGGER.info("Call to UOM update meth....");
		int updateState = 0;
		try {
			getSelectedUom().setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			updateState = this.uomService.updateUom(getSelectedUom());

			if (updateState > 0) {
				LOGGER.info("Update Success...");
				addMessage("Update UOM", "Update UOM Successfull");
				loadAllUom();
				clearFiled();
			} else {

				addErrorMessage("Update UOM", "Update UOM Faild");
			}
		} catch (Exception e) {
			LOGGER.error("Update UOM Fail", e);
			addErrorMessage("Update UOM", "Update UOM Faild\n" + e.getLocalizedMessage());
		}
	}

	// Delete Uom Function
	public void deleteUom() {
		LOGGER.info("Call to delete UOM method....");
		int deleteState = 0;

		try {
			deleteState = this.uomService.deleteUom(getDeleteUom());

			if (deleteState > 0) {
				LOGGER.info("Delete UOM Success...");
				addMessage("Delete UOM", "Delete UOM Successfull");
				loadAllUom();
			} else {
				addErrorMessage("Delete UOM", "Delete UOM Faild");
			}
		} catch (Exception e) {
			LOGGER.error("Delete UOM Fail", e);
			addErrorMessage("Delete UOM", "Delete UOM Faild\n" + e.getLocalizedMessage());
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

	private void clearFiled() {
		getSelectedUom().setUomName(null);
		getSelectedUom().setUomChar(null);
	
	}

	public Uom getSelectedUom() {
		return selectedUom;
	}

	public void setSelectedUom(Uom selectedUom) {
		this.selectedUom = selectedUom;
	}

	public Uom getDeleteUom() {
		return deleteUom;
	}

	public void setDeleteUom(Uom deleteUom) {
		this.deleteUom = deleteUom;
		deleteUom();
	}

	public List<Uom> getUomList() {
		return uomList;
	}

	public void setUomList(List<Uom> uomList) {
		this.uomList = uomList;
	}

	public List<Uom> getFilteredUom() {
		return filteredUom;
	}

	public void setFilteredUom(List<Uom> filteredUom) {
		this.filteredUom = filteredUom;
	}

}
