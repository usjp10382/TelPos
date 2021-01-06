package com.teleios.pos.controls;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.nio.file.AccessDeniedException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teleios.pos.dao.utill.TeleiosPosConstant;
import com.teleios.pos.model.Brand;
import com.teleios.pos.model.Category;
import com.teleios.pos.model.Product;
import com.teleios.pos.model.SmsModel;
import com.teleios.pos.model.Uom;
import com.teleios.pos.service.BrandService;
import com.teleios.pos.service.CategoryService;
import com.teleios.pos.service.ProductService;
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
	@Autowired
	private ProductService productService;
	@Autowired
	private RestTemplate restTemplate;

	// Required Helper List Of Objects
	private List<Product> allActiveProducts;
	private List<Product> filteredPrdList;
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
	private boolean cartEmpty = true;
	private Product havRemObj;

	// Delete And Update Product
	private Product havUpdateProduct;
	private Product havDeleteProduct;

	@PostConstruct
	public void init() {
		LOGGER.info("<------------ Execute Product Controller Init ------------->");
		loadAllActiveProducts();
		try {
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

	private void loadAllActiveProducts() {
		try {
			this.allActiveProducts = this.productService.getAllActiveProducts();
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Cant Connect To Database", ste);
			addWarMessage("Load All Active Product", "Database Connection Failed");
		} catch (EmptyResultDataAccessException empe) {
			LOGGER.error("Init Load Error", empe);
			addWarMessage("Init Load All Active Prodcuts", "Doesnt Contains Any Products");
		} catch (DataAccessException dae) {
			LOGGER.error("Loead All Active Product Daa Acc Error", dae);
			addErrorMessage("Init Load All Active Prodcuts Error",
					"Data AccesError Ocured-->" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Unexpected Error---", e);
			addErrorMessage("Init Load All Active Prodcuts", "System Error Ocured-->" + e.getLocalizedMessage());
		}
	}

	// Filter Function For Product Table
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		Product product = (Product) value;

		return product.getPrdName().toLowerCase().contains(filterText)
				|| product.getPrdCode().toLowerCase().contains(filterText);
	}

	public void createNewProduct() {
		LOGGER.info("<---------- Execute Create New Product -------->");
		int saveState = 0;
		try {
			if (getSelectedBrand() == null) {
				addErrorMessage("Create New Product", "Select Product Brand Is Required!");
				return;
			}
			if (getSelectedCategory() == null) {
				addErrorMessage("Create New Product", "Select Product Category Is Required!");
				return;
			}
			if (getSelectedUom() == null) {
				addErrorMessage("Create New Product", "Select Product UOM Is Required!");
				return;
			}
			if (getNewProduct() != null) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null) {
					getNewProduct().setCreateBy(auth.getName());
				} else {
					throw new AccessDeniedException("Un Authorized Access !");
				}

				if (getNewProduct().getMinQtyLevel() == null) {
					addErrorMessage("Create New Product", "Minimumm Quantity Level Is Required!");
					return;
				}

				if (getNewProduct().getMinQtyLevel() < 0.0) {
					addErrorMessage("Create New Product", "Minimumm Quantity Level Canot'be Minus!");
					return;
				}

				if (getNewProduct().getRackDet().isEmpty()) {
					getNewProduct().setRackDet("N/A");
				}

				getNewProduct().setCreateDate(new Date());
				getNewProduct().setBrand(getSelectedBrand());
				getNewProduct().setCategory(getSelectedCategory());
				getNewProduct().setUom(getSelectedUom());

				saveState = this.productService.createNewProduct(getNewProduct());
				if (saveState > 0) {
					addMessage("Create New Product", "Succesfuly Create New Prodcut!");
					clearFiled(0);
					loadAllActiveProducts();
				} else {
					addErrorMessage("Create New Product", "Create New Product Failed !");
				}
			}
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Couldnt Connect Database", ste);
			addErrorMessage("Create New Product", "Couldn't Connect To Database\n" + ste.getLocalizedMessage());
		} catch (DuplicateKeyException dke) {
			LOGGER.error("Entered Prodcut Key Duplicate", dke);
			addErrorMessage("Create New Product", "Entered Product Code Allready Exist");
		} catch (Exception e) {
			LOGGER.error("Create New Prodcut Error", e);
			addErrorMessage("Create New Product", "Create New Product Error\n" + e.getLocalizedMessage());
		}
	}

	public void createNewProducts() {
		LOGGER.info("<------- Execute Create All Products ------------->");
		int[] saveState;
		try {
			if (getProductsCart() != null) {
				if (getProductsCart().size() <= 0) {
					addErrorMessage("Create Batch Of Products", "Doesent Contain Any Products In Cart !");
					return;
				}
				saveState = this.productService.createNewProduct(getProductsCart());
				if (saveState.length > 0) {
					addMessage("Create Batch Of Products",
							"Succesfuly Create Number Of-> " + saveState.length + " Products !");
					clearFiled(1);
					loadAllActiveProducts();
					setCartEmpty(true);
				} else {
					addErrorMessage("Create Bulk Of Products", "Create Prodcuts Failed !");
				}
			}
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Couldnt Connect Database", ste);
			addErrorMessage("Create New Products", "Couldn't Connect To Database\n" + ste.getLocalizedMessage());
		} catch (DuplicateKeyException dke) {
			LOGGER.error("Entered Prodcut Key Duplicate", dke);
			addErrorMessage("Create New Products", "Entered Product Code Allready Exist");
		} catch (Exception e) {
			LOGGER.error("Create New Prodcuts Error", e);
			addErrorMessage("Create New Product", "Create New Product Error\n" + e.getLocalizedMessage());
		}
	}

	public void addToCart() {
		LOGGER.info("<---------- Execute Add To Product Cart -------->");
		try {
			Thread.sleep(5000);
			if (getProductsCart().size() >= 30) {
				addErrorMessage("Product Add To Cart", "Product Cart Size Is Exeed Plz Save Added Items To System!");
				return;
			}
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

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			if (auth != null) {
				product.setCreateBy(auth.getName());
			} else {
				throw new AccessDeniedException("Un Authorized Access !");
			}

			if (getNewProduct().getMinQtyLevel() == null) {
				addErrorMessage("Create New Product", "Minimumm Quantity Level Is Required!");
				return;
			}

			if (getNewProduct().getMinQtyLevel() < 0.0) {
				addErrorMessage("Create New Product", "Minimumm Quantity Level Canot'be Minus!");
				return;
			}

			if (getNewProduct().getRackDet().isEmpty()) {
				product.setRackDet("N/A");
			} else {
				product.setRackDet(getNewProduct().getRackDet());
			}

			product.setBrand(getSelectedBrand());
			product.setCategory(getSelectedCategory());
			product.setCreateDate(new Date());
			product.setPrdCode(getNewProduct().getPrdCode().trim().toLowerCase());
			product.setPrdName(getNewProduct().getPrdName().trim());
			product.setState((short) 1);
			product.setUom(getSelectedUom());
			product.setMinQtyLevel(getNewProduct().getMinQtyLevel());

			if (isDuplicateOfDb(product)) {
				addErrorMessage("Add New Product To Cart",
						"Entered Product Code Allready Exist In System\n" + product.getPrdCode());
				return;
			}

			if (isDuplicateOfCart(product)) {
				addErrorMessage("Add New Product To Cart",
						"Entered Product Code Allready Exist In Cart\n" + product.getPrdCode());
				return;
			}

			if (this.getProductsCart().add(product)) {
				addMessage("Product Add To Cart", "Product Successfuly Added To Cart!");
				setCartEmpty(false);
				clearFiled(0);
			} else {
				addErrorMessage("Product Add To Cart", "Product  Added To Cart Failed!");
			}

		} catch (Exception e) {
			LOGGER.error("Product Add To Cart Error---> ", e);
			addErrorMessage("New Product Add To Cart", "Error Ocured!\n" + e.getLocalizedMessage());
		}
	}

	public void removeFromCart() {
		LOGGER.info("<--------- Execute Remove Product From  Cart ----------->");
		Iterator<Product> iterator;
		try {
			if (getProductsCart().size() <= 0)
				return;
			iterator = getProductsCart().iterator();

			while (iterator.hasNext()) {
				Product product = iterator.next();

				if (getHavRemObj() == product) {
					iterator.remove();
					break;
				}

			}
			if (getProductsCart().isEmpty())
				setCartEmpty(true);

			addMessage("Remove Product From Cart", "Successfuly Removed !");

		} catch (Exception e) {
			LOGGER.error("Remove  Product From Cart Error--->", e);
			addErrorMessage("Create New Products", "Remove Product From  Cart Error\n" + e.getLocalizedMessage());
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

	private void clearFiled(final int flag) {
		LOGGER.info("<------ Clear Product Added Det Called --------->");
		try {
			if (getNewProduct() != null) {
				this.newProduct.setPrdCode(null);
				this.newProduct.setPrdName(null);
				this.newProduct.setRackDet(null);
				this.newProduct.setMinQtyLevel(null);
			}
			this.selectedBrand = null;
			this.selectedCategory = null;
			this.selectedUom = null;

			if (flag == 1)
				getProductsCart().clear();

		} catch (Exception e) {
			LOGGER.error("Prodcut Added List Clear Error--->", e);
			addErrorMessage("Clear Product Input", "Error Ocured\n" + e.getLocalizedMessage());
		}
	}

	private boolean isDuplicateOfDb(final Product chkPrd) {
		LOGGER.info("<-------- Execute Product Is Duplicate From DB --------> ");
		Iterator<Product> iterator = null;
		boolean isDuplicate = false;
		try {
			if (getAllActiveProducts() != null) {

				if (getAllActiveProducts().size() <= 0) {
					return isDuplicate;
				}
				iterator = getAllActiveProducts().iterator();
				while (iterator.hasNext()) {
					Product product = iterator.next();
					if (chkPrd.getPrdCode().equalsIgnoreCase(product.getPrdCode())) {
						isDuplicate = true;
						break;
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error("Check Is Duplicate From DB Error--> ", e);
			addErrorMessage("Add New Prodcut To Cart", "Check Duplicate From DB Error\n" + e.getLocalizedMessage());
		}

		return isDuplicate;
	}

	private boolean isDuplicateOfCart(final Product chkPrd) {
		LOGGER.info("<-------- Execute Product Is Duplicate From Cart --------> ");
		Iterator<Product> iterator = null;
		boolean isDuplicate = false;
		try {
			if (getProductsCart() != null) {

				if (getProductsCart().size() <= 0) {
					return isDuplicate;
				}
				iterator = getProductsCart().iterator();
				while (iterator.hasNext()) {
					Product product = iterator.next();
					if (chkPrd.getPrdCode().equalsIgnoreCase(product.getPrdCode())) {
						isDuplicate = true;
						break;
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error("Check Is Duplicate From Cart Error--> ", e);
			addErrorMessage("Add New Prodcut To Cart", "Check Duplicate From Cart Error\n" + e.getLocalizedMessage());
		}

		return isDuplicate;
	}

	/*
	 * @PreDestroy public void clearAll() {
	 * LOGGER.info("*********** Pre Destroy Called ********************"); }
	 */

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

	public List<Product> getAllActiveProducts() {
		return allActiveProducts;
	}

	public void setAllActiveProducts(List<Product> allActiveProducts) {
		this.allActiveProducts = allActiveProducts;
	}

	public boolean isCartEmpty() {
		return cartEmpty;
	}

	public void setCartEmpty(boolean cartEmpty) {
		this.cartEmpty = cartEmpty;
	}

	public Product getHavRemObj() {
		return havRemObj;
	}

	public void setHavRemObj(Product havRemObj) {
		this.havRemObj = havRemObj;
		removeFromCart();
	}

	public Product getHavUpdateProduct() {
		return havUpdateProduct;
	}

	public void setHavUpdateProduct(Product havUpdateProduct) {
		this.havUpdateProduct = havUpdateProduct;
		if (getHavUpdateProduct() != null) {
			setSelectedBrand(getHavUpdateProduct().getBrand());
			setSelectedCategory(getHavUpdateProduct().getCategory());
			setSelectedUom(getHavUpdateProduct().getUom());
			testSmS();
		}
	}

	public Product getHavDeleteProduct() {
		return havDeleteProduct;
	}

	public void setHavDeleteProduct(Product havDeleteProduct) {
		this.havDeleteProduct = havDeleteProduct;
	}

	private void testSmS() {
		LOGGER.info("<------ Execute Test Mapper ----->");
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			SmsModel smsModel = new SmsModel();

			smsModel.setSource("Teleios");
			// smsModel.setDestinations(new String[] { "94717624597", "94716155228" });
			smsModel.setDestinations(new String[] { "94716155228" });
			smsModel.setTransports(new String[] { "sms" });
			Map<String, String> content = new HashMap<String, String>();
			content.put("sms", "This Is Test From Teleios");
			smsModel.setContent(content);

			String jsonInsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(smsModel);

			LOGGER.info("Maper JSON {}", jsonInsString);

			// RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", TeleiosPosConstant.SMS_API_KEY);

			HttpEntity<String> request = new HttpEntity<String>(jsonInsString, headers);

			ResponseEntity<String> response = this.restTemplate.exchange(TeleiosPosConstant.SMS_BASE_URL,
					HttpMethod.POST, request, String.class);

			String json = response.getBody();

			LOGGER.info("sms responce----> {}", json);

		} catch (Exception e) {
			LOGGER.error("Mapper Error---> ", e);
		}
	}

}
