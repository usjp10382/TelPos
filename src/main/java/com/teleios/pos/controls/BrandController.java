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

import com.teleios.pos.model.Brand;
import com.teleios.pos.service.BrandService;

@Named("brandController")
@ViewScoped
public class BrandController implements Serializable {
	/**
	 * @author Harith Ahangama
	 * @Date 2020.12.12
	 * @Discription Manage bean for control Brand
	 * @since 1.0
	 */
	private static final long serialVersionUID = -6555774381203321112L;
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);

	@Autowired
	private BrandService brandService;

	private Brand selectedBrand = new Brand();
	private Brand deleteBrand = new Brand();
	private List<Brand> brandList;
	private List<Brand> filteredBrands;

	@PostConstruct
	public void init() {
		LOGGER.info("<-----------Execute BrandController Init------------->");
		loadAllCategories();
	}

	private void loadAllCategories() {
		try {
			getSelectedBrand().setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			getSelectedBrand().setCreateDate(new Date());
			this.brandList = brandService.getActiveBrands();
		} catch (EmptyResultDataAccessException empe) {
			LOGGER.error("Loead All Brands Is Emplty", empe);
			addWarMessage("Brand Operation Init", "Doesnt Contains Any Brand/s");
		} catch (DataAccessException dae) {
			LOGGER.error("Loead All Brand Is Daa Acc Error", dae);
			addErrorMessage("Brand Operation Init", "Data AccesError Ocured-->" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Unexpected Error---", e);
			addErrorMessage("Brand Operation Init", "System Error Ocured-->" + e.getLocalizedMessage());
		}
	}

	// Filter Function For Brands Table
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		LOGGER.info("<----Global Filter Function Called----->");
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		Brand brand = (Brand) value;

		return brand.getBrandName().toLowerCase().contains(filterText)
				|| brand.getCreateBy().toLowerCase().contains(filterText);
	}

	// Brand Save Function
	public void creaeNewBrand() {
		LOGGER.info("Execute Create New Brand--------->");
		int saveState = 0;
		try {
			getSelectedBrand().setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			getSelectedBrand().setCreateDate(new Date());
			saveState = this.brandService.createNewBrand(getSelectedBrand());
			if (saveState > 0) {
				LOGGER.info("<--------- Create New Brand Success--------->");
				addMessage("Create New Brand", "Create New Brand Success!");
				loadAllCategories();
				clearFiled();
			} else {
				addErrorMessage("Create New Brand", "Create New Brand Falied");
			}

		} catch (DuplicateKeyException de) {
			LOGGER.error("Create New Brand Name Duplicate Ocurr----------> ", de);
			addErrorMessage("Create New Brand", "Entered Brand Name Allredy Exit\n" + de.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Create New Brand Error Ocurr----------> ", e);
			addErrorMessage("Create New Brand", "Create New Brand Falied\n" + e.getLocalizedMessage());
		}
	}

	// Brand Update Function
	public void updateBrand() {
		LOGGER.info("Call to Brand update meth....");
		int updateState = 0;
		try {
			getSelectedBrand().setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			updateState = this.brandService.updateBrand(getSelectedBrand());

			if (updateState > 0) {
				LOGGER.info("Update Brand Success...");
				addMessage("Update Brand", "Update Brand Successfull!");
				loadAllCategories();
				clearFiled();
			} else {

				addErrorMessage("Update Brand", "Update Brand Faild!");
			}
		} catch (DuplicateKeyException de) {
			LOGGER.error("Update Brand Name Duplicate Ocurr----------> ", de);
			addErrorMessage("Update  Brand", "Entered Brand Name Allredy Exit\n" + de.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Update Brand Fail", e);
			addErrorMessage("Update Brand", "Update Brand Faild\n" + e.getLocalizedMessage());
		}
	}

	// Delete Brand Function
	public void deleteBrand() {
		LOGGER.info("Call to delete Brand method....");
		int deleteState = 0;

		try {
			deleteState = this.brandService.deleteBrand(getDeleteBrand());

			if (deleteState > 0) {
				LOGGER.info("Delete Brand Success...");
				addMessage("Delete Brand", "Delete Brand Successfull");
				loadAllCategories();
			} else {
				LOGGER.info("Delete Brand Failed...");
				addErrorMessage("Delete Brand", "Delete Brand Faild");
			}
		} catch (Exception e) {
			LOGGER.error("Delete Brand Fail", e);
			addErrorMessage("Delete Brand", "Delete Brand Faild\n" + e.getLocalizedMessage());
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
		getSelectedBrand().setBrandName(null);

	}

	public Brand getSelectedBrand() {
		return selectedBrand;
	}

	public void setSelectedBrand(Brand selectedBrand) {
		this.selectedBrand = selectedBrand;
	}

	public Brand getDeleteBrand() {
		return deleteBrand;
	}

	public void setDeleteBrand(Brand deleteBrand) {
		this.deleteBrand = deleteBrand;
		deleteBrand();
	}

	public List<Brand> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Brand> brandList) {
		this.brandList = brandList;
	}

	public List<Brand> getFilteredBrands() {
		return filteredBrands;
	}

	public void setFilteredBrands(List<Brand> filteredBrands) {
		this.filteredBrands = filteredBrands;
	}

}
