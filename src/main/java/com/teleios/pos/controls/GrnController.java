package com.teleios.pos.controls;

import java.io.IOException;
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
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.mail.MessagingException;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teleios.pos.dao.utill.TeleiosPosConstant;
import com.teleios.pos.model.CashPayment;
import com.teleios.pos.model.CheqType;
import com.teleios.pos.model.GrnDet;
import com.teleios.pos.model.GrnHdr;
import com.teleios.pos.model.PaymentType;
import com.teleios.pos.model.Product;
import com.teleios.pos.model.Supplier;
import com.teleios.pos.service.EmailService;
import com.teleios.pos.service.GrnService;
import com.teleios.pos.service.ProductService;
import com.teleios.pos.service.ReportService;
import com.teleios.pos.service.SupplierService;

import net.sf.jasperreports.engine.JRException;

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
	@Autowired
	private EmailService emailService;

	// Utility Object
	private List<PaymentType> paymentTypes;
	private List<CheqType> cheqTypes;
	private Date minExpDate = new Date();
	private boolean txtCashPayLock;
	private boolean txtCheckLock;
	private boolean btnPrintLock;
	private boolean btnSendMailBlock;
	private Integer oldGrnNumber;

	// Grn Header Related Selected Objects
	private GrnHdr grnHdr;
	private Integer nextGrnNumber;
	private Integer nextBatchNumber;
	private Supplier selSuppliyer;
	private PaymentType selPayType;
	private CheqType selChequType;

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
		this.txtCashPayLock = true;
		this.txtCheckLock = true;
		this.btnSendMailBlock = true;
		this.btnPrintLock = true;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			grnHdr.setCreateBy(auth.getName());
		} else {
			throw new AccessDeniedException("Un Authorized Access !");
		}

		this.grnHdr.setGrnDate(new Date());

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
		if (currentStepId.equalsIgnoreCase("tabTwo")) {
			if (getGrnDets().size() <= 0) {
				addErrorMessage("Create New GRN", "Add Grn Items Is Required!");
				return currentStepId;
			}
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
			setGrnHeaderDetails(grnDet);
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
			if (getOldGrnNumber() != null) {
				this.reportService.printGrnReportByNumber(getOldGrnNumber());
			} else {
				addErrorMessage("Print GRN Report", "Couldnt Find Old GRN ");
			}
		} catch (IOException ioe) {
			LOGGER.error("Print Grn Report By Number IO Exception Error Occured-->", ioe);
			addErrorMessage("Print GRN Report",
					"Couldnt Find Report Call To System Admin\n" + ioe.getLocalizedMessage());
		} catch (JRException jre) {
			LOGGER.error("Print Grn Report By Number Error Occured-->", jre);
			addErrorMessage("Print GRN Report",
					"System Error Occured Call To System Admin\n" + jre.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Print Grn Report By Number Error Occured-->", e);
			addErrorMessage("Print GRN Report",
					"System Error Occured Call To System Admin\n" + e.getLocalizedMessage());
		}
	}

	public void sendEmail() {
		LOGGER.info("<------Execute Send GRN As Email In Grn Controller -------->");
		try {
			if (getOldGrnNumber() != null) {
				this.emailService.sendMail(getOldGrnNumber());
				addMessage("Send GRN E-mail", "Successfuly Send Email!");
			} else {
				addErrorMessage("Send GRN E-mail", "Couldnt Find Old GRN ");
			}
		} catch (IOException ioe) {
			LOGGER.error("Send Email Grn Report By Number IO Exception Error Occured-->", ioe);
			addErrorMessage("Send GRN Report",
					"Couldnt Find Report Call To System Admin\n" + ioe.getLocalizedMessage());
		} catch (JRException jre) {
			LOGGER.error("Send Grn Report By Number Error Occured-->", jre);
			addErrorMessage("Send GRN Report",
					"System Error Occured Call To System Admin\n" + jre.getLocalizedMessage());
		} catch (MessagingException e) {
			LOGGER.error("Send Grn Report By Number Error Occured-->", e);
			addErrorMessage("Send GRN Report", "System Error Occured Call To System Admin\n" + e.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Send GRN As Email Error Occured ---->", e);
			addErrorMessage("Send Grn Email", "Error Occured\n" + e.getLocalizedMessage());
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
				getGrnHdr().setTotalValue(getGrnHdr().getTotalValue().subtract(getRemGrnDet().getSubTotal()));
				getGrnHdr().setPaybleAmount(getGrnHdr().getPaybleAmount().subtract(getRemGrnDet().getSubTotal()));
				getGrnHdr().setBalance(getGrnHdr().getBalance().subtract(getRemGrnDet().getSubTotal()));
				addMessage("Create New Grn.. Remove GRN Item From Cart", "Succsesfuly Remove GRN Item");
			}
		} catch (Exception e) {
			LOGGER.error("Remove GRN Item From Cart Error", e);
			addErrorMessage("Create New Grn", "Remove GRN Item Error\n" + e.getLocalizedMessage());
		}
	}

	private void setGrnHeaderDetails(final GrnDet newGrnDet) {
		if (getGrnHdr().getTotalValue() == null) {
			getGrnHdr().setTotalValue(newGrnDet.getSubTotal());
		} else {
			getGrnHdr().setTotalValue(getGrnHdr().getTotalValue().add(newGrnDet.getSubTotal()));
		}
		if (getGrnHdr().getPaybleAmount() != null) {
			getGrnHdr().setPaybleAmount(getGrnHdr().getPaybleAmount().add(newGrnDet.getSubTotal()));
		} else {
			getGrnHdr().setPaybleAmount(newGrnDet.getSubTotal());
		}
		if (getGrnHdr().getBalance() != null) {
			getGrnHdr().setBalance(getGrnHdr().getBalance().add(newGrnDet.getSubTotal()));
		} else {
			getGrnHdr().setBalance(newGrnDet.getSubTotal());
		}

	}

	public void handleDiscountKeyEvent(ValueChangeEvent evt) {
		BigDecimal discounValue = BigDecimal.ZERO;
		BigDecimal initTotal = new BigDecimal(getGrnHdr().getTotalValue().doubleValue());
		try {
			LOGGER.info("DIscount Value Hash Code Test: initTotal---> " + initTotal + " Global Total ---> "
					+ getGrnHdr().getTotalValue());
			getGrnHdr().setGrnValDiscount((BigDecimal) evt.getNewValue());
			if (getGrnHdr().getGrnValDiscount().compareTo(BigDecimal.ZERO) == 0) {
				getGrnHdr().setPaybleAmount(initTotal);
				getGrnHdr().setBalance(initTotal);
				getGrnHdr().setTotalValue(initTotal);
				getGrnHdr().setTotValDiscount(false);
			}
			if (getGrnHdr().getGrnValDiscount() != null) {
				discounValue = grnHdr.getTotalValue()
						.multiply(getGrnHdr().getGrnValDiscount().divide(BigDecimal.valueOf(100.00)));
				getGrnHdr().setPaybleAmount(getGrnHdr().getTotalValue().subtract(discounValue));
				getGrnHdr().setBalance(getGrnHdr().getTotalValue().subtract(discounValue));
				getGrnHdr().setTotValDiscount(true);
			} else {
				LOGGER.info("Cut Zero Discount: {}", discounValue);
				getGrnHdr().setPaybleAmount(initTotal);
				getGrnHdr().setBalance(initTotal);
				getGrnHdr().setTotalValue(initTotal);
				getGrnHdr().setTotValDiscount(false);
			}
			LOGGER.info("Discount Value : {}", discounValue);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Enter Discount Invalied Input", nfe);
			addMessage("Enter Discount", "Invalied Input\n" + nfe.getLocalizedMessage());
		} catch (ArithmeticException ame) {
			LOGGER.error("Enter Discount Arithmetic Exception", ame);
			addMessage("Enter Discount", "Arithmetic Exception\n" + ame.getLocalizedMessage());
		} catch (NullPointerException npe) {
			getGrnHdr().setPaybleAmount(initTotal);
			getGrnHdr().setBalance(initTotal);
			getGrnHdr().setTotalValue(initTotal);
			getGrnHdr().setTotValDiscount(false);
		} catch (Exception e) {
			LOGGER.error("System Error Occured", e);
			addMessage("Enter Discount", "System Error Occured\n" + e.getLocalizedMessage());
		}
	}

	public void onPayTypeChange() {
		LOGGER.info("Execute Payment Type Change Ajax------->");
		try {
			if (getSelPayType() != null) {
				switch (getSelPayType().getPayTypeId()) {
				case TeleiosPosConstant.CHEQUE:
					addErrorMessage("Select Payment Type", "Operation Notyet Added!");
					break;

				case TeleiosPosConstant.CREDICT:
					addErrorMessage("Select Payment Type", "Operation Notyet Added!");
					break;

				case TeleiosPosConstant.CASH:
					getGrnHdr().setPaidAmount(getGrnHdr().getPaybleAmount());
					getGrnHdr().setBalance(BigDecimal.ZERO);
					break;

				case TeleiosPosConstant.CASHANDCHEQUE:
					addErrorMessage("Select Payment Type", "Operation Notyet Added!");
					break;

				case TeleiosPosConstant.CASHANDCREDI:
					addErrorMessage("Select Payment Type", "Operation Notyet Added!");
					break;

				default:
					addErrorMessage("Select Payment Type", "Invalied Payment Type!");
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error("On CHange Payment Type Error--->", e);
			addErrorMessage("Select Payment Type", "System Error Occured!\n" + e.getLocalizedMessage());
		}
	}

	public void createNewGrn() {
		LOGGER.info("<------- Execute Create New Grn In Grn Controller --------->");

		if (getSelSuppliyer() == null) {
			addErrorMessage("Create New GRN", "The Select Suppliyer Is Required!");
			return;
		}

		if (getGrnHdr() == null) {
			addErrorMessage("Create New GRN", "Please Follow Step by Step To Create GRN!");
			return;
		}

		if (getGrnDets().size() <= 0) {
			addErrorMessage("Create New GRN", "The Grn Doesnt Contains Any Item!");
			return;
		}

		if (getSelPayType() == null) {
			addErrorMessage("Create New GRN", "The Select Payment Type Is Required!");
			return;
		}

		switch (getSelPayType().getPayTypeId()) {
		case TeleiosPosConstant.CHEQUE:
			addErrorMessage("Select Payment Type", "Operation Notyet Added!");
			break;

		case TeleiosPosConstant.CREDICT:
			addErrorMessage("Select Payment Type", "Operation Notyet Added!");
			break;

		case TeleiosPosConstant.CASH:
			try {

				getGrnHdr().setSupplier(getSelSuppliyer());
				getGrnHdr().setGrnNumber(getNextGrnNumber());
				getGrnHdr().setBatchNumber(getNextBatchNumber());
				getGrnHdr().setItemCount(getGrnDets().size());
				getGrnHdr().setGrnState((short) 1);
				getGrnHdr().setCreateDate(new Date());
				getGrnHdr().setGrnDets(getGrnDets());
				getGrnHdr().setPaymentType(getSelPayType());

				CashPayment cashPayment = new CashPayment();
				cashPayment.setAmount(getGrnHdr().getPaidAmount());
				cashPayment.setCashier(getGrnHdr().getPaidAmount());
				cashPayment.setChange(BigDecimal.ZERO);
				cashPayment.setCreateDate(new Date());
				cashPayment.setPayeble(null);
				cashPayment.setCreateBy(getGrnHdr().getCreateBy());
				cashPayment.setState((short) 1);
				cashPayment.setGrnHdr(getGrnHdr());
				cashPayment.setExpenditureList(null);

				this.grnService.createNewCashPayGrn(getGrnHdr(), cashPayment);
				addMessage("Create New Grn", "Successfuly Create New GRN!");
				clearAll();
				loadNextBatchAndGrnNumbers();

			} catch (SocketTimeoutException sce) {
				LOGGER.error("Create New GRN Couldnt Connect TO Database", sce);
				addErrorMessage("Create New GRN Confirm",
						"Couldnt Connect To Database\nPlease Inform To System Admin !\n" + sce.getLocalizedMessage());
			} catch (DataAccessException dae) {
				LOGGER.error("Create New GRN Couldnt Connect TO Database", dae);
				addErrorMessage("Create New GRN Confirm",
						"Dataaccess Error Occured\nPlease Inform To System Admin !\n" + dae.getLocalizedMessage());
			} catch (Exception e) {
				LOGGER.error("Create New Grn Error Unexpced System Error", e);
				addErrorMessage("Create New GRN Confirm",
						"Unexpected System Error Please\nInform To System Admin!\n" + e.getLocalizedMessage());
			}
			break;

		case TeleiosPosConstant.CASHANDCHEQUE:
			addErrorMessage("Select Payment Type", "Operation Notyet Added!");
			break;

		case TeleiosPosConstant.CASHANDCREDI:
			addErrorMessage("Select Payment Type", "Operation Notyet Added!");
			break;

		default:
			addErrorMessage("Select Payment Type", "Invalied Payment Type!");
			break;
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

	private void clearAll() {
		LOGGER.info("Execute Clear All In Grn Controller ------>");
		try {
			this.oldGrnNumber = new Integer(getNextGrnNumber());
			this.grnHdr = null;
			this.grnDets.clear();
			this.txtCashPayLock = true;
			this.txtCheckLock = true;
			this.btnSendMailBlock = false;
			this.btnPrintLock = false;
			this.selPayType = null;
			this.selSuppliyer = null;
			this.grnHdr = new GrnHdr();
			this.grnHdr.setGrnDate(new Date());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				grnHdr.setCreateBy(auth.getName());
			} else {
				throw new AccessDeniedException("Un Authorized Access !");
			}
			subNumber = 1;
		} catch (Exception e) {
			LOGGER.error("Grn Clear All Error", e);
			addErrorMessage("Create New GRN", "Reset Created Grn Error\n" + e.getLocalizedMessage());
		}
	}

	public List<Product> getAllActivePrloducts() {
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

	/**
	 * @return the txtCashPayLock
	 */
	public boolean isTxtCashPayLock() {
		return txtCashPayLock;
	}

	/**
	 * @param txtCashPayLock the txtCashPayLock to set
	 */
	public void setTxtCashPayLock(boolean txtCashPayLock) {
		this.txtCashPayLock = txtCashPayLock;
	}

	/**
	 * @return the txtCheckLock
	 */
	public boolean isTxtCheckLock() {
		return txtCheckLock;
	}

	/**
	 * @param txtCheckLock the txtCheckLock to set
	 */
	public void setTxtCheckLock(boolean txtCheckLock) {
		this.txtCheckLock = txtCheckLock;
	}

	/**
	 * @return the btnPrintLock
	 */
	public boolean isBtnPrintLock() {
		return btnPrintLock;
	}

	/**
	 * @param btnPrintLock the btnPrintLock to set
	 */
	public void setBtnPrintLock(boolean btnPrintLock) {
		this.btnPrintLock = btnPrintLock;
	}

	/**
	 * @return the btnSendMailBlock
	 */
	public boolean isBtnSendMailBlock() {
		return btnSendMailBlock;
	}

	/**
	 * @param btnSendMailBlock the btnSendMailBlock to set
	 */
	public void setBtnSendMailBlock(boolean btnSendMailBlock) {
		this.btnSendMailBlock = btnSendMailBlock;
	}

	public List<Product> getAllActiveProducts() {
		return allActiveProducts;
	}

	public Integer getOldGrnNumber() {
		return oldGrnNumber;
	}

	public void setOldGrnNumber(Integer oldGrnNumber) {
		this.oldGrnNumber = oldGrnNumber;
	}

}
