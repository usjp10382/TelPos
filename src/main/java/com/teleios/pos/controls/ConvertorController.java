/**
	 * @author Harith Ahangama
	 * @Date 2020.12.12
	 * @Discription Manage bean for control Converter
	 */
package com.teleios.pos.controls;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
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

	private boolean cartEmpty = true;
	private List<Convertor> allActiveConvertors;
	private List<Convertor> filteredConvertor;
	private List<Convertor> convertorCart = new LinkedList<Convertor>();

	private Integer selBaseUomId;
	private Integer selDerUomId;
	private Convertor havRemObj;
	private Convertor selcDelConvertor;

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

	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		LOGGER.info("<----Global Filter Function Called----->");
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}
		Convertor convertor = (Convertor) value;

		return convertor.getBaseUom().getUomChar().toLowerCase().contains(filterText)
				|| convertor.getBaseUom().getUomChar().toLowerCase().contains(filterText);
	}

	public void addToCart() {
		LOGGER.info("<-----Execute Convertor Add To Cart----->");
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
			if (getNewConvertorObj().getValue() <= 0.0) {
				addErrorMessage("Create New Convertor Item", "Ratio Value Shuld Be Gretter Than Zero!");
				return;
			}

			Convertor newConvertor = new Convertor();

			newConvertor.setBaseUom(getSelUomObj(getSelBaseUomId()));
			newConvertor.setRatUom(getSelUomObj(getSelDerUomId()));
			newConvertor.setValue(Double.valueOf(getNewConvertorObj().getValue()));
			newConvertor.setCreateby(SecurityContextHolder.getContext().getAuthentication().getName());
			newConvertor.setCreateDate(new Date());
			newConvertor.setState((short) 1);
			if (checkIsDuplicateConvertor(newConvertor)) {
				addErrorMessage("Create New Convertor", "Entered Convertor Combination Allready Exist In Database");
				return;
			}
			if (isDuplicateConvCombination(newConvertor)) {
				addErrorMessage("Add New Convertors To Cart", "Duplicate Convertor Combination");
			} else {
				getConvertorCart().add(newConvertor);
				setCartEmpty(false);
				clearInput(0);
				addMessage("Add New Convertors To Cart", "New Converor Successfuly Added !");
			}

		} catch (Exception e) {
			LOGGER.error("Add To Cart Convertor Error Ocured--->", e);
			addErrorMessage("Create New Convertor Item", "Add To Cart New Converto Error\n" + e.getLocalizedMessage());
		}
	}

	public void removeFromCart() {
		LOGGER.info("<--------- Execute Remove From Convertor Cart ----------->");
		Iterator<Convertor> iterator;
		try {
			if (getConvertorCart().size() <= 0)
				return;
			iterator = getConvertorCart().iterator();

			while (iterator.hasNext()) {
				Convertor chekedConvertor = iterator.next();

				if (getHavRemObj() == chekedConvertor) {
					iterator.remove();
					break;
				}

			}
			if (getConvertorCart().isEmpty())
				setCartEmpty(true);

			addMessage("Remove Convertors From Cart", "Successfuly Removed !");

		} catch (Exception e) {
			LOGGER.error("Remove From Convertor Cart Error--->", e);
			addErrorMessage("Create New Convertor", "Remove From Convertor Cart Error\n" + e.getLocalizedMessage());
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

			if (getNewConvertorObj().getValue() <= 0.0) {
				addErrorMessage("Create New Convertor Item", "Ratio Value Shuld Be Gretter Than Zero!");
				return;
			}

			getNewConvertorObj().setBaseUom(getSelUomObj(getSelBaseUomId()));
			getNewConvertorObj().setRatUom(getSelUomObj(getSelDerUomId()));
			getNewConvertorObj().setCreateby(SecurityContextHolder.getContext().getAuthentication().getName());
			getNewConvertorObj().setCreateDate(new Date());
			getNewConvertorObj().setState((short) 1);

			if (checkIsDuplicateConvertor(getNewConvertorObj())) {
				addErrorMessage("Create New Convertor", "Entered Convertor Combination Allready Exist In Database");
				return;
			}

			createState = this.convertorService.createNewConvertor(getNewConvertorObj());
			if (createState > 0) {
				addMessage("Create New Convertor Item", "Successfuly Create New Convertor Item !");
				clearInput(0);
				loadAllActiveConvertors();
			} else {
				addErrorMessage("Create New Convertor Item", "Create New Convertor Item Is Failed !");
			}

		} catch (Exception e) {
			LOGGER.error("Create New Convertor Errorr Ocurr--> ", e);
			addErrorMessage("Create New Convertor Item",
					"Create New Convertor Item Is Failed !\n" + e.getLocalizedMessage());
		}
	}

	public void createAllConvertors() {
		LOGGER.info("<----------- Execute Create All Convertors ------------>");
		int[] batchSize;
		try {
			if (getAllActiveConvertors().isEmpty()) {
				addErrorMessage("Create Batch Of Convertors", "Doesent Contains Any COnvertors");
				return;
			}
			batchSize = this.convertorService.createNewConvertor(getConvertorCart());

			if (batchSize.length > 0) {
				addMessage("Create Batch Of Convertors", "Create Batch Of Convertors Succcess !");
				clearInput(1);
			} else {
				addErrorMessage("Create Batch Of Convertors", "Create Batch Of Convertors Failed!");
			}
		} catch (Exception e) {
			LOGGER.error("Create All Convertors Error--> ", e);
			addErrorMessage("Create Batch Of Convertors",
					"Create Batch Of Convertors Error\n" + e.getLocalizedMessage());
		}
	}

	public void deleteConvertor() {
		LOGGER.info("<---------------- Execute Delete Convertor -------------->");
		int deleteState = 0;
		try {
			deleteState = this.convertorService.deleteConvertor(getSelcDelConvertor());
			if (deleteState > 0) {
				addMessage("Delete Convertor", "Delete Convertor Success!");
				loadAllActiveConvertors();
			} else {
				addErrorMessage("Delete Convertor", "Delete Convertor Failed !");
			}
		} catch (Exception e) {
			LOGGER.error("Delete Convertor Error---->", e);
			addErrorMessage("Delete Convertor", "Delete Convertor Error Ocured!\n" + e.getLocalizedMessage());
		}

	}

	private boolean checkIsDuplicateConvertor(final Convertor checkConvertor) {
		boolean isDuplicate = false;
		Iterator<Convertor> iterator = null;
		try {
			if (getAllActiveConvertors() != null) {
				if (getAllActiveConvertors().size() <= 0)
					return isDuplicate;

				iterator = getAllActiveConvertors().iterator();
				while (iterator.hasNext()) {
					Convertor convertor = iterator.next();
					if ((checkConvertor.getBaseUom().getUomId().equals(convertor.getBaseUom().getUomId()))
							&& (checkConvertor.getRatUom().getUomId().equals(convertor.getRatUom().getUomId()))) {
						isDuplicate = true;
						break;
					}

				}

			}

		} catch (Exception e) {
			LOGGER.info("Check Is Duplicate Combination Error---> ", e);
			addErrorMessage("Create New Convertor", "Check Duplicate Combination Error\n" + e.getLocalizedMessage());
		}
		return isDuplicate;
	}

	private boolean isDuplicateConvCombination(final Convertor convertor) {
		boolean isDuplicate = false;
		Iterator<Convertor> iterator;
		try {
			if (this.convertorCart.size() <= 0)
				return isDuplicate;

			iterator = convertorCart.iterator();
			while (iterator.hasNext()) {
				Convertor checkConvertor = iterator.next();

				if ((convertor.getBaseUom().getUomId().equals(checkConvertor.getBaseUom().getUomId()))
						&& (convertor.getRatUom().getUomId().equals(checkConvertor.getRatUom().getUomId()))) {
					isDuplicate = true;
					break;
				}

			}

		} catch (Exception e) {
			LOGGER.error("Check Duplicate Combinaton Error---> ", e);
			addErrorMessage("Create New Convertor Item",
					"Check Duplicate Convertor Combination Error !\n" + e.getLocalizedMessage());
		}
		return isDuplicate;
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

	private void clearInput(final int flag) {
		try {
			this.selBaseUomId = null;
			this.selDerUomId = null;
			this.newConvertorObj.setValue(null);
			if (flag == 1) {
				getConvertorCart().clear();
				loadAllActiveConvertors();
				setCartEmpty(true);
			}
		} catch (Exception e) {
			LOGGER.error("Clear Input Error--> ", e);
			addErrorMessage("Create New Convertor", "Clear Input System Error\n" + e.getLocalizedMessage());
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

	public boolean isCartEmpty() {
		return cartEmpty;
	}

	public void setCartEmpty(boolean cartEmpty) {
		this.cartEmpty = cartEmpty;
	}

	public List<Convertor> getFilteredConvertor() {
		return filteredConvertor;
	}

	public void setFilteredConvertor(List<Convertor> filteredConvertor) {
		this.filteredConvertor = filteredConvertor;
	}

	public List<Convertor> getConvertorCart() {
		return convertorCart;
	}

	public void setConvertorCart(List<Convertor> convertorCart) {
		this.convertorCart = convertorCart;
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

	public Convertor getHavRemObj() {
		return havRemObj;
	}

	public void setHavRemObj(Convertor havRemObj) {
		this.havRemObj = havRemObj;
		removeFromCart();
	}

	public Convertor getNewConvertorObj() {
		return newConvertorObj;
	}

	public void setNewConvertorObj(Convertor newConvertorObj) {
		this.newConvertorObj = newConvertorObj;
	}

	public Convertor getSelcDelConvertor() {
		return selcDelConvertor;
	}

	public void setSelcDelConvertor(Convertor selcDelConvertor) {
		this.selcDelConvertor = selcDelConvertor;
		deleteConvertor();
	}

}
