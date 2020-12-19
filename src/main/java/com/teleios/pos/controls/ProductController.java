package com.teleios.pos.controls;

import java.io.Serializable;
import java.util.Date;
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

import com.teleios.pos.model.Brand;
import com.teleios.pos.model.Category;
import com.teleios.pos.model.Product;
import com.teleios.pos.model.Uom;
import com.teleios.pos.service.BrandService;
import com.teleios.pos.service.CategoryService;
import com.teleios.pos.service.UomService;

@Named("productController")
@ViewScoped
public class ProductController implements Serializable {
	private static final long serialVersionUID = 2993540332188987445L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	// Injected Services
	@Autowired
	private BrandService brandService;
	@Autowired
	private UomService uomService;
	@Autowired
	private CategoryService categoryService;

	// Required Helper List Of Objects
	private List<Brand> allActiveBrand;
	private List<Category> allActiveCategories;
	private List<Uom> allActiveUoms;

	// Selected Object
	private Brand selectedBrand;
	private Category selectedCategory;
	private Uom selectedUom;

	// New Product Object and Cart List
	private Product newProduct = new Product();
	private List<Product> productsCart = new LinkedList<Product>();
	private List<Product> filteredPrdList;

	@PostConstruct
	public void init() {
		LOGGER.info("<------------ Execute Product Controller Init ------------->");

		try {
			this.newProduct.setCreateBy(SecurityContextHolder.getContext().getAuthentication().getName());
			this.newProduct.setCreateDate(new Date());
			this.allActiveBrand = this.brandService.getActiveBrands();
			this.allActiveCategories = this.categoryService.getActiveCategories();
			this.allActiveUoms = this.uomService.getActiveUoms();

		} catch (EmptyResultDataAccessException empe) {
			LOGGER.error("Init Load Error", empe);
			addWarMessage("Init Load Error", "Doesnt Contains Any Category/s Or UOMS Or Brands Or All");
		} catch (DataAccessException dae) {
			LOGGER.error("Loead All Categories Is Daa Acc Error", dae);
			addErrorMessage("Init Load Error", "Data AccesError Ocured-->" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Unexpected Error---", e);
			addErrorMessage("Init Load Error", "System Error Ocured-->" + e.getLocalizedMessage());
		}
	}

	// Filter Function For Product Table
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		LOGGER.info("<----Global Filter Function Called----->");
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		Product product = (Product) value;

		return product.getPrdName().toLowerCase().contains(filterText)
				|| product.getPrdCode().toLowerCase().contains(filterText);
	}

	public void addToCart() {
		LOGGER.info("<---------- Execute Add To Product Cart -------->");
		try {
			if (getSelectedBrand() == null) {
				addErrorMessage("Product Add To Cart", "Select Product Brand Is Required!");
				return;
			}
			if (getSelectedCategory() == null) {
				addErrorMessage("Product Add To Cart", "Select Product Category Is Required!");
				return;
			}
			if (getSelectedUom() == null) {
				addErrorMessage("Product Add To Cart", "Select Product UOM Is Required!");
				return;
			}
			Product product = new Product();
			product.setBrand(getSelectedBrand());
			product.setCategory(getSelectedCategory());
			product.setCreateBy(getNewProduct().getCreateBy());
			product.setCreateDate(new Date());
			product.setPrdCode(getNewProduct().getPrdCode().trim());
			product.setPrdName(getNewProduct().getPrdName().trim());
			product.setState((short) 1);
			product.setUom(getSelectedUom());

			if (this.getProductsCart().add(product)) {
				addMessage("Product Add To Cart", "Product Successfuly Added To Cart!");
				clearFiled();
			} else {
				addErrorMessage("Product Add To Cart", "Product  Added To Cart Failed!");
			}

		} catch (Exception e) {
			LOGGER.error("Product Add To Cart Error---> ", e);
			addErrorMessage("New Product Add To Cart", "Error Ocured!\n" + e.getLocalizedMessage());
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
		LOGGER.info("<------ Clear Product Added Det Called --------->");
		try {
			if (getNewProduct() != null) {
				this.newProduct.setPrdCode(null);
				this.newProduct.setPrdName(null);
			}
			this.selectedBrand = null;
			this.selectedCategory = null;
			this.selectedUom = null;
		} catch (Exception e) {
			LOGGER.error("Prodcut Added List Clear Error--->", e);
			addErrorMessage("Clear Product Input", "Error Ocured\n" + e.getLocalizedMessage());
		}
	}

	public BrandService getBrandService() {
		return brandService;
	}

	public void setBrandService(BrandService brandService) {
		this.brandService = brandService;
	}

	public UomService getUomService() {
		return uomService;
	}

	public void setUomService(UomService uomService) {
		this.uomService = uomService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public List<Brand> getAllActiveBrand() {
		return allActiveBrand;
	}

	public void setAllActiveBrand(List<Brand> allActiveBrand) {
		this.allActiveBrand = allActiveBrand;
	}

	public List<Category> getAllActiveCategories() {
		return allActiveCategories;
	}

	public void setAllActiveCategories(List<Category> allActiveCategories) {
		this.allActiveCategories = allActiveCategories;
	}

	public List<Uom> getAllActiveUoms() {
		return allActiveUoms;
	}

	public void setAllActiveUoms(List<Uom> allActiveUoms) {
		this.allActiveUoms = allActiveUoms;
	}

	public Brand getSelectedBrand() {
		return selectedBrand;
	}

	public void setSelectedBrand(Brand selectedBrand) {
		this.selectedBrand = selectedBrand;
	}

	public Category getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(Category selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public Uom getSelectedUom() {
		return selectedUom;
	}

	public void setSelectedUom(Uom selectedUom) {
		this.selectedUom = selectedUom;
	}

	public Product getNewProduct() {
		return newProduct;
	}

	public void setNewProduct(Product newProduct) {
		this.newProduct = newProduct;
	}

	public List<Product> getProductsCart() {
		return productsCart;
	}

	public void setProductsCart(List<Product> productsCart) {
		this.productsCart = productsCart;
	}

	public List<Product> getFilteredPrdList() {
		return filteredPrdList;
	}

	public void setFilteredPrdList(List<Product> filteredPrdList) {
		this.filteredPrdList = filteredPrdList;
	}

}
