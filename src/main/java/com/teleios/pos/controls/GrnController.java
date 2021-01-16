package com.teleios.pos.controls;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teleios.pos.model.CheqType;
import com.teleios.pos.model.GrnDet;
import com.teleios.pos.model.GrnHdr;
import com.teleios.pos.model.PaymentType;
import com.teleios.pos.model.Product;
import com.teleios.pos.model.Supplier;
import com.teleios.pos.service.GrnService;
import com.teleios.pos.service.ProductService;
import com.teleios.pos.service.ReportService;
import com.teleios.pos.service.SupplierService;

@Named("grnController")
@ViewScoped
public class GrnController implements Serializable {
	private static final long serialVersionUID = 3977298484279299411L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GrnController.class);

	// Inject Utility Service
	@Autowired
	private ProductService productService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private GrnService grnService;
	@Autowired
	private ReportService reportService;

	// Utility Object
	private List<PaymentType> paymentTypes;
	private List<CheqType> cheqTypes;
	private Date minExpDate = new Date();

	// Grn Header Related Selected Objects
	private GrnHdr grnHdr;
	private Integer nextGrnNumber;
	private Integer nextBatchNumber;
	private Supplier selSuppliyer;
	private PaymentType selPayType;
	private CheqType selChequType;
	private BigDecimal grnValDiscount;

	// Grn Details Related Selected Object
	private List<GrnDet> grnDets;
	private Product selProduct;
	private BigDecimal qty;
	private BigDecimal untiPrice;
	private BigDecimal discount;
	private BigDecimal mrp;
	private Date expDate;
	private int subNumber = 1;
	private GrnDet remGrnDet;

	// Required Helper List Of Objects
	private List<Product> allActiveProducts;
	private List<Supplier> allActiveSuppliyers;

	@PostConstruct
	public void init() throws AccessDeniedException {
		LOGGER.info("<--- Inizilalising Grn Controller ........");

		this.grnHdr = new GrnHdr();
		this.grnDets = new LinkedList<GrnDet>();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			grnHdr.setCreateBy(auth.getName());
		} else {
			throw new AccessDeniedException("Un Authorized Access !");
		}

		this.grnHdr.setCreateDate(new Date());

		loadPaymentTypes();
		loadNextBatchAndGrnNumbers();
		loadAllActiveProducts();
		loadAllActiveSuppliyers();
	}

	private void loadPaymentTypes() {
		try {
			this.paymentTypes = this.grnService.getPaymentTypes((short) 6);
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Cant Connect To Database", ste);
			addWarMessage("Load PaymentType", "Database Connection Failed");
		} catch (EmptyResultDataAccessException empe) {
			LOGGER.error("Init Load Error", empe);
			addWarMessage("Init Load PaymentType", "Doesnt Contains Any PaymentTypes");
		} catch (DataAccessException dae) {
			LOGGER.error("Loead PaymentType Daa Acc Error", dae);
			addErrorMessage("Init Load PaymentType Error", "Data AccesError Ocured-->" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Unexpected Error---", e);
			addErrorMessage("Init Load PaymentType", "System Error Ocured-->" + e.getLocalizedMessage());
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

	private void loadAllActiveSuppliyers() {
		LOGGER.debug("<---- Execute Load All Active Suppliyers In Suppliyer Controllers ------>");
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

	private void loadNextBatchAndGrnNumbers() {
		LOGGER.debug("<---- Execute Load All Load Next Batch And GRN Numbers Controllers ------>");
		try {
			this.nextGrnNumber = this.grnService.getNextGrnNumber();
			this.nextBatchNumber = this.grnService.getNextBatchNumber();
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

	public List<Product> completeProductsContains(String query) {
		String queryLowerCase = query.toLowerCase();
		return getAllActiveProducts().stream().filter(p -> p.getPrdCode().toLowerCase().contains(queryLowerCase)
				|| p.getPrdName().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
	}

	public void onItemSelect(SelectEvent<Product> event) {
		LOGGER.info("On Selected Prd----> {}", event.getObject().getPrdName());
	}

	public String handleFlow(FlowEvent event) {
		String currentStepId = event.getOldStep();
		String nextStepId = event.getNewStep();
		if (currentStepId.equalsIgnoreCase("tabTwo")) {
			if (getGrnDets().size() <= 0) {
				addErrorMessage("Create New GRN", "Add Grn Items Is Required!");
				return currentStepId;
			}
		}
		if (nextStepId.equalsIgnoreCase("payDet")) {
			setonfirmDetails();
		}
		return event.getNewStep();
	}

	public void addToCart() {
		LOGGER.info("<------- Execute Add To Cart Grn Details ----->");
		BigDecimal unPriceafterAdjustDisc = new BigDecimal(0.0);
		BigDecimal discValue = new BigDecimal(0.0);
		boolean isDiscount = false;
		if (getSelProduct() == null) {
			addErrorMessage("Create New GRN", "The Item Select Is Required!");
			return;
		}

		if (getQty() == null) {
			addErrorMessage("Create New GRN", "The Quantity Is Required!");
			return;
		}
		if (getQty().compareTo(BigDecimal.ZERO) <= 0) {
			addErrorMessage("Create New GRN", "The Quantity Cant Zero Or Minus!");
			return;
		}
		if (getUntiPrice() == null) {
			addErrorMessage("Create New GRN", "The Unit Price Is Required!");
			return;
		}
		if (getUntiPrice().compareTo(BigDecimal.ZERO) == 0 || getUntiPrice().compareTo(BigDecimal.ZERO) == -1) {
			addErrorMessage("Create New GRN", "The Unit Price Canot be Zero Or Minus!");
			return;
		}
		if (getDiscount() != null) {
			if (getDiscount().compareTo(BigDecimal.ZERO) == 0 || getDiscount().compareTo(BigDecimal.ZERO) == -1) {
				addErrorMessage("Create New GRN", "The Discount Canot be Zero Or Minus!");
				return;
			}
		}

		if (getMrp() == null) {
			addErrorMessage("Create New GRN", "The MRP Is Required!");
			return;
		}

		if (getMrp().compareTo(BigDecimal.ZERO) == 0 || getMrp().compareTo(BigDecimal.ZERO) == -1) {
			addErrorMessage("Create New GRN", "The MRP Canot be Zero Or Minus!");
			return;
		}

		try {
			if (getDiscount() != null) {
				isDiscount = true;
				unPriceafterAdjustDisc = getUntiPrice()
						.subtract(getUntiPrice().multiply(getDiscount().divide(BigDecimal.valueOf(100))));
				discValue = getUntiPrice().subtract(unPriceafterAdjustDisc);

				if (getUntiPrice().subtract(unPriceafterAdjustDisc).compareTo(getMrp()) == 1) {
					addErrorMessage("Create New GRN", "The MRP Is Less Thaana Unit Price!");
					return;
				}

			} else {
				if (getUntiPrice().compareTo(getMrp()) == 1) {
					addErrorMessage("Create New GRN", "The MRP Is Less Thaana Unit Price!");
					return;
				} else if (getUntiPrice().compareTo(getMrp()) == 0) {
					addWarMessage("Create New GRN", "WARNING !The Unit Price And MRP Are Same!");
				}
			}
			GrnHdr grnHdr = new GrnHdr();
			grnHdr.setGrnNumber(nextGrnNumber);
			GrnDet grnDet = new GrnDet(new Integer(subNumber), getSelProduct(), getQty(), getUntiPrice(), isDiscount,
					getDiscount(), getUntiPrice().subtract(discValue), getMrp(),
					getQty().multiply(getUntiPrice().subtract(discValue)), getExpDate(), (short) 1, grnHdr);

			if (isCartDuplicate(grnDet)) {
				addErrorMessage("Create New Grn", "Duplicate Item Details");
				return;
			}

			if (this.grnDets.add(grnDet)) {
				addMessage("Create New GRN", "New Grn Item Added Success!");
				this.subNumber++;
				clearFiled();
			}

		} catch (ArithmeticException ae) {
			LOGGER.error("Calculation Error..", ae);
			addErrorMessage("Create New Grn", "Error Occured,,Incorrect Detail\n" + ae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Add New GRN Item Error..", e);
			addErrorMessage("Create New Grn", "Error Occured,\n" + e.getLocalizedMessage());
		}

	}

	public void printGrn() {
		LOGGER.info("Execute Print Grn In Controller ------>");
		try {
			this.reportService.printAllItemsReport();
		} catch (Exception e) {
			LOGGER.error("Error Occured-->", e);
		}
	}

	private boolean isCartDuplicate(final GrnDet grnDet) {
		LOGGER.info("Execute Is Duplicate Product");
		boolean isDuplicate = false;
		Iterator<GrnDet> iterator;
		if (getGrnDets().size() <= 0) {
			return isDuplicate;
		}
		try {
			iterator = getGrnDets().iterator();
			while (iterator.hasNext()) {
				GrnDet havCheckGrnDet = iterator.next();
				if (grnDet.getUnitPrice().equals(havCheckGrnDet.getUnitPrice())
						& grnDet.getProduct().getPrdId().equals(havCheckGrnDet.getProduct().getPrdId())) {
					isDuplicate = true;
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Check Is Duplicate Product Error", e);
			addErrorMessage("Create New Grn", "Check Is Duplicate Product Error\n" + e.getLocalizedMessage());
		}
		return isDuplicate;
	}

	private void removeFromCart() {
		LOGGER.info("Execute Reomve Grn Det From Cart ----->");
		try {
			if (getGrnDets().remove(getRemGrnDet())) {
				addMessage("Create New Grn.. Remove GRN Item From Cart", "Succsesfuly Remove GRN Item");
			}
		} catch (Exception e) {
			LOGGER.error("Remove GRN Item From Cart Error", e);
			addErrorMessage("Create New Grn", "Remove GRN Item Error\n" + e.getLocalizedMessage());
		}
	}

	private void setonfirmDetails() {
		LOGGER.info("<------- Execute Set Confirm Details-------->");
		try {
			BigDecimal grnTotal = BigDecimal.ZERO;
			BigDecimal paybAmount = BigDecimal.ZERO;
			this.grnValDiscount = BigDecimal.ZERO;

			this.grnHdr.setGrnNumber(getNextGrnNumber());
			this.grnHdr.setBatchNumber(getNextBatchNumber());

			for (GrnDet det : getGrnDets()) {
				grnTotal = paybAmount.add(det.getNetPrice().multiply(det.getQty()));
				paybAmount = paybAmount.add(det.getNetPrice().multiply(det.getQty()));
			}
			grnHdr.setTotalValue(grnTotal);
			grnHdr.setGrnValDiscount(getGrnValDiscount());
			grnHdr.setPaybleAmount(paybAmount);
			grnHdr.setBalance(paybAmount);

		} catch (Exception e) {
			LOGGER.error("Create New GRN Set Confirm Error..", e);
			addErrorMessage("Create New GRN", "Set Confirm Page Error\n" + e.getLocalizedMessage());
		}

	}

	public void handleDiscountKeyEvent(AjaxBehaviorEvent evt) {
		BigDecimal discounValue = BigDecimal.ZERO;
		BigDecimal initTotal = new BigDecimal(getGrnHdr().getTotalValue().doubleValue());
		BigDecimal initPayblVal = new BigDecimal(getGrnHdr().getPaybleAmount().doubleValue());
		BigDecimal initBalance = new BigDecimal(getGrnHdr().getBalance().doubleValue());
		try {
			if (getGrnHdr().getGrnValDiscount() != null) {
				LOGGER.info("Grn Discount Precentage :{}", getGrnHdr().getGrnValDiscount());
				discounValue = initTotal.multiply(getGrnHdr().getGrnValDiscount().divide(BigDecimal.valueOf(100.00)));
				LOGGER.info("Grn Discount Value:{}", discounValue);
				getGrnHdr().setPaybleAmount(getGrnHdr().getPaybleAmount().subtract(discounValue));
				getGrnHdr().setBalance(getGrnHdr().getBalance().subtract(discounValue));
			} else {
				LOGGER.info("Cut Zero Discount: {}", discounValue);
				getGrnHdr().getPaybleAmount().add(initPayblVal);
				getGrnHdr().getBalance().add(initBalance);
			}
		} catch (NumberFormatException nfe) {
			LOGGER.error("Enter Discount Invalied Input", nfe);
			addMessage("Enter Discount", "Invalied Input\n" + nfe.getLocalizedMessage());
		} catch (ArithmeticException ame) {
			LOGGER.error("Enter Discount Arithmetic Exception", ame);
			addMessage("Enter Discount", "Arithmetic Exception\n" + ame.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("System Error Occured", e);
			addMessage("Enter Discount", "System Error Occured\n" + e.getLocalizedMessage());
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
		LOGGER.info("Execute Clear Grn Filed -----");
		try {
			this.selProduct = null;
			this.qty = null;
			this.expDate = null;
			this.discount = null;
			this.untiPrice = null;
			this.mrp = null;
		} catch (Exception e) {
			LOGGER.error("Clear Filed Error--", e);
			addMessage("Create New Grn", "Flied Clear Error Ocuured!\n" + e.getLocalizedMessage());
		}
	}

	public List<Product> getAllActiveProducts() {
		return allActiveProducts;
	}

	public void setAllActiveProducts(List<Product> allActiveProducts) {
		this.allActiveProducts = allActiveProducts;
	}

	public List<Supplier> getAllActiveSuppliyers() {
		return allActiveSuppliyers;
	}

	public void setAllActiveSuppliyers(List<Supplier> allActiveSuppliyers) {
		this.allActiveSuppliyers = allActiveSuppliyers;
	}

	public List<PaymentType> getPaymentTypes() {
		return paymentTypes;
	}

	public void setPaymentTypes(List<PaymentType> paymentTypes) {
		this.paymentTypes = paymentTypes;
	}

	public List<CheqType> getCheqTypes() {
		return cheqTypes;
	}

	public void setCheqTypes(List<CheqType> cheqTypes) {
		this.cheqTypes = cheqTypes;
	}

	public Supplier getSelSuppliyer() {
		return selSuppliyer;
	}

	public GrnHdr getGrnHdr() {
		return grnHdr;
	}

	public void setGrnHdr(GrnHdr grnHdr) {
		this.grnHdr = grnHdr;
	}

	public void setSelSuppliyer(Supplier selSuppliyer) {
		this.selSuppliyer = selSuppliyer;
	}

	public PaymentType getSelPayType() {
		return selPayType;
	}

	public void setSelPayType(PaymentType selPayType) {
		this.selPayType = selPayType;
	}

	public CheqType getSelChequType() {
		return selChequType;
	}

	public void setSelChequType(CheqType selChequType) {
		this.selChequType = selChequType;
	}

	public Integer getNextGrnNumber() {
		return nextGrnNumber;
	}

	public void setNextGrnNumber(Integer nextGrnNumber) {
		this.nextGrnNumber = nextGrnNumber;
	}

	public Integer getNextBatchNumber() {
		return nextBatchNumber;
	}

	public void setNextBatchNumber(Integer nextBatchNumber) {
		this.nextBatchNumber = nextBatchNumber;
	}

	// Getters And Setters For Grn Details

	public Product getSelProduct() {
		return selProduct;
	}

	public void setSelProduct(Product selProduct) {
		this.selProduct = selProduct;
	}

	/**
	 * @return the qty
	 */
	public BigDecimal getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	/**
	 * @return the untiPrice
	 */
	public BigDecimal getUntiPrice() {
		return untiPrice;
	}

	/**
	 * @param untiPrice the untiPrice to set
	 */
	public void setUntiPrice(BigDecimal untiPrice) {
		this.untiPrice = untiPrice;
	}

	/**
	 * @return the discount
	 */
	public BigDecimal getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	/**
	 * @return the mrp
	 */
	public BigDecimal getMrp() {
		return mrp;
	}

	/**
	 * @param mrp the mrp to set
	 */
	public void setMrp(BigDecimal mrp) {
		this.mrp = mrp;
	}

	/**
	 * @return the expDate
	 */
	public Date getExpDate() {
		return expDate;
	}

	/**
	 * @param expDate the expDate to set
	 */
	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public Date getMinExpDate() {
		return minExpDate;
	}

	public void setMinExpDate(Date minExpDate) {
		this.minExpDate = minExpDate;
	}

	public List<GrnDet> getGrnDets() {
		return grnDets;
	}

	public void setGrnDets(List<GrnDet> grnDets) {
		this.grnDets = grnDets;
	}

	public GrnDet getRemGrnDet() {
		return remGrnDet;
	}

	public void setRemGrnDet(GrnDet remGrnDet) {
		this.remGrnDet = remGrnDet;
		removeFromCart();
	}

	public BigDecimal getGrnValDiscount() {
		return grnValDiscount;
	}

	public void setGrnValDiscount(BigDecimal grnValDiscount) {
		this.grnValDiscount = grnValDiscount;
	}

}
