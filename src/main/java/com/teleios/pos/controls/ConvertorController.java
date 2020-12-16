/**
	 * @author Harith Ahangama
	 * @Date 2020.12.12
	 * @Discription Manage bean for control Converter
	 */
package com.teleios.pos.controls;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import org.springframework.security.core.context.SecurityContextHolder;

import com.teleios.pos.model.Convertor;
import com.teleios.pos.model.Uom;
import com.teleios.pos.service.ConvertorService;
import com.teleios.pos.service.UomService;

@Named("convertorController")
@ViewScoped
public class ConvertorController implements Serializable {

	private static final long serialVersionUID = -4889998676755311383L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ConvertorController.class);

	// Inject Service Classes
	@Autowired
	private UomService uomService;
	@Autowired
	private ConvertorService convertorService;

	// Define Uom Variables
	private List<Uom> uoms;

	private List<Convertor> allActiveConvertors;

	private Integer selBaseUomId;
	private Integer selDerUomId;

	private Convertor newConvertorObj = new Convertor();

	@PostConstruct
	public void init() {
		LOGGER.info("<--------- Execute Convertor Bean Init ----------->");
		loadAllActiveUoms();
		loadAllActiveConvertors();
	}

	private void loadAllActiveUoms() {
		try {
			this.uoms = this.uomService.getActiveUoms();
		} catch (EmptyResultDataAccessException empe) {
			LOGGER.error("Loead All UOMS Is Emplty", empe);
			addWarMessage("Convertor Operation Init", "Doesnt Contains Any UOMS/s");
		} catch (DataAccessException dae) {
			LOGGER.error("Loead All UOMS Is Daa Acc Error", dae);
			addErrorMessage("Convertor Operation Init", "Data AccesError Ocured-->" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Unexpected Error---", e);
			addErrorMessage("Convertor Operation Init", "System Error Ocured-->" + e.getLocalizedMessage());
		}
	}

	private void loadAllActiveConvertors() {
		try {
			this.allActiveConvertors = this.convertorService.getActiveConvertors();
		} catch (EmptyResultDataAccessException empe) {
			LOGGER.error("Loead All Active Convertors Is Emplty", empe);
			addWarMessage("Convertor Operation Init", "Doesnt Contains Any Convertor/s");
		} catch (DataAccessException dae) {
			LOGGER.error("Loead All Active Convertors Is Daa Acc Error", dae);
			addErrorMessage("Convertor Operation Init", "Data AccesError Ocured-->" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Unexpected Error---", e);
			addErrorMessage("Convertor Operation Init", "System Error Ocured-->" + e.getLocalizedMessage());
		}
	}

	public void createNewConvertor() {
		LOGGER.info("<----Execute Create New Convertor------>");
		int createState = 0;
		try {
			if (this.uoms == null) {
				return;
			}
			if (this.uoms.size() <= 0) {
				return;
			}
			if (getSelBaseUomId() == null) {
				addErrorMessage("Create New Convertor Item", "Select Base Unit Is Required!");
				return;
			}
			if (getSelDerUomId() == null) {
				addErrorMessage("Create New Convertor Item", "Select Derived Unit Is Required!");
				return;
			}
			if (getSelBaseUomId().equals(getSelDerUomId())) {
				addErrorMessage("Create New Convertor Item", "The Derived Unit Can't Be the Base Unti!");
				return;
			}

			getNewConvertorObj().setBaseUom(getSelUomObj(getSelBaseUomId()));
			getNewConvertorObj().setRatUom(getSelUomObj(getSelDerUomId()));
			getNewConvertorObj().setCreateby(SecurityContextHolder.getContext().getAuthentication().getName());
			getNewConvertorObj().setCreateDate(new Date());
			getNewConvertorObj().setState((short) 1);

			createState = this.convertorService.createNewConvertor(getNewConvertorObj());
			if (createState > 0) {
				addMessage("Create New Convertor Item", "Successfuly Create New Convertor Item !");
			} else {
				addErrorMessage("Create New Convertor Item", "Create New Convertor Item Is Failed !");
			}

		} catch (Exception e) {
			LOGGER.error("Create New Convertor Errorr Ocurr--> ", e);
			addErrorMessage("Create New Convertor Item",
					"Create New Convertor Item Is Failed !\n" + e.getLocalizedMessage());
		}
	}

	private Uom getSelUomObj(final Integer checkUom) {
		Uom selObj = null;
		try {
			if (uoms != null) {
				if (uoms.size() > 0) {
					Iterator<Uom> iterator = this.uoms.iterator();
					while (iterator.hasNext()) {
						Uom uom = iterator.next();
						if (uom.getUomId().equals(checkUom)) {
							selObj = uom;
							break;
						}

					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Search Selected Uom Error--> ", e);
			addErrorMessage("Create New Convertor Item",
					"Search Selected Uom Item ErrorOcurr\n" + e.getLocalizedMessage());
		}
		return selObj;
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

	public List<Uom> getUoms() {
		return uoms;
	}

	public void setUoms(List<Uom> uoms) {
		this.uoms = uoms;
	}

	public List<Convertor> getAllActiveConvertors() {
		return allActiveConvertors;
	}

	public void setAllActiveConvertors(List<Convertor> allActiveConvertors) {
		this.allActiveConvertors = allActiveConvertors;
	}

	public Integer getSelBaseUomId() {
		return selBaseUomId;
	}

	public void setSelBaseUomId(Integer selBaseUomId) {
		this.selBaseUomId = selBaseUomId;
	}

	public Integer getSelDerUomId() {
		return selDerUomId;
	}

	public void setSelDerUomId(Integer selDerUomId) {
		this.selDerUomId = selDerUomId;
	}

	public Convertor getNewConvertorObj() {
		return newConvertorObj;
	}

	public void setNewConvertorObj(Convertor newConvertorObj) {
		this.newConvertorObj = newConvertorObj;
	}

}
