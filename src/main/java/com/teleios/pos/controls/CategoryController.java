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

import com.teleios.pos.model.Category;
import com.teleios.pos.service.CategoryService;

@Named("categController")
@ViewScoped
public class CategoryController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5324689614403275797L;

	/**
	 * @author Harith Ahangama
	 * @Date 2020.12.12
	 * @Discription Manage bean for control Category
	 * @since 1.0
	 */

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private CategoryService categoryService;

	private Category selectedCategory = new Category();
	private Category deleteCategory = new Category();
	private List<Category> categoryList;
	private List<Category> filteredCategories;

	@PostConstruct
	public void init() {
		LOGGER.info("<-----------Execute CategoryController Init------------->");
		loadAllCategories();
	}

	private void loadAllCategories() {
		try {
			getSelectedCategory().setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			getSelectedCategory().setCreateDate(new Date());
			this.categoryList = categoryService.getActiveCategories();
		} catch (EmptyResultDataAccessException empe) {
			LOGGER.error("Loead All Categories Is Emplty", empe);
			addWarMessage("Category Operation Init", "Doesnt Contains Any Category/s");
		} catch (DataAccessException dae) {
			LOGGER.error("Loead All Categories Is Daa Acc Error", dae);
			addErrorMessage("Category Operation Init", "Data AccesError Ocured-->" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Unexpected Error---", e);
			addErrorMessage("Category Operation Init", "System Error Ocured-->" + e.getLocalizedMessage());
		}
	}

	// Filter Function For Categories Table
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		LOGGER.info("<----Global Filter Function Called----->");
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		Category uom = (Category) value;

		return uom.getCategoryName().toLowerCase().contains(filterText)
				|| uom.getCreateBy().toLowerCase().contains(filterText);
	}

	// Category Save Function
	public void creaeNewCategory() {
		LOGGER.info("Execute Create New Category--------->");
		int saveState = 0;
		try {
			getSelectedCategory().setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			getSelectedCategory().setCreateDate(new Date());
			saveState = this.categoryService.createNewCategory(getSelectedCategory());
			if (saveState > 0) {
				LOGGER.info("<--------- Create New Category Success--------->");
				addMessage("Create New Category", "Create New Category Success!");
				loadAllCategories();
				clearFiled(0);
			} else {
				addErrorMessage("Create New Category", "Create New Category Falied");
			}

		} catch (DuplicateKeyException de) {
			LOGGER.error("Create New Category Name Duplicate Ocurr----------> ", de);
			addErrorMessage("Create New Category", "Entered Category Name Allredy Exit\n" + de.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Create New Category Error Ocurr----------> ", e);
			addErrorMessage("Create New Category", "Create New Category Falied\n" + e.getLocalizedMessage());
		}
	}

	// Category Update Function
	public void updateCategory() {
		LOGGER.info("Call to Category update meth....");
		int updateState = 0;
		try {
			getSelectedCategory().setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			updateState = this.categoryService.updateCategory(getSelectedCategory());

			if (updateState > 0) {
				LOGGER.info("Update Category Success...");
				addMessage("Update Category", "Update Category Successfull!");
				loadAllCategories();
				clearFiled(1);
			} else {

				addErrorMessage("Update Category", "Update Category Faild!");
			}
		} catch (Exception e) {
			LOGGER.error("Update Category Fail", e);
			addErrorMessage("Update Category", "Update Category Faild\n" + e.getLocalizedMessage());
		}
	}

	// Delete Category Function
	public void deleteCategory() {
		LOGGER.info("Call to delete Category method....");
		int deleteState = 0;

		try {
			deleteState = this.categoryService.deleteCategory(getDeleteCategory());

			if (deleteState > 0) {
				LOGGER.info("Delete Category Success...");
				addMessage("Delete Category", "Delete Category Successfull");
				loadAllCategories();
			} else {
				LOGGER.info("Delete Category Failed...");
				addErrorMessage("Delete Category", "Delete Category Faild");
			}
		} catch (Exception e) {
			LOGGER.error("Delete Category Fail", e);
			addErrorMessage("Delete Category", "Delete Category Faild\n" + e.getLocalizedMessage());
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

	private void clearFiled(int flag) {
		getSelectedCategory().setCategoryName(null);
		if (flag == 1) {
			getSelectedCategory().setCreateBy(null);
			getSelectedCategory().setCreateDate(null);
		}

	}

	public Category getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(Category selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public Category getDeleteCategory() {
		return deleteCategory;
	}

	public void setDeleteCategory(Category deleteCategory) {
		this.deleteCategory = deleteCategory;
		deleteCategory();
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public List<Category> getFilteredCategories() {
		return filteredCategories;
	}

	public void setFilteredCategories(List<Category> filteredCategories) {
		this.filteredCategories = filteredCategories;
	}

}
