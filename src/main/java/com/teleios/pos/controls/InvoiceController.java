package com.teleios.pos.controls;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketTimeoutException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teleios.pos.dao.utill.TeleiosPosConstant;
import com.teleios.pos.model.CashRecived;
import com.teleios.pos.model.CheqType;
import com.teleios.pos.model.Customer;
import com.teleios.pos.model.InvDet;
import com.teleios.pos.model.InvHdr;
import com.teleios.pos.model.PaymentType;
import com.teleios.pos.model.Stock;
import com.teleios.pos.service.CustomerService;
import com.teleios.pos.service.GrnService;
import com.teleios.pos.service.InvoiceService;
import com.teleios.pos.service.StockService;

@Named("invController")
@ViewScoped
public class InvoiceController implements Serializable {
	private static final long serialVersionUID = -6376989656853356566L;
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);

	// Injected OBJ
	@Autowired
	private CustomerService customerService;
	@Autowired
	private StockService stockService;
	@Autowired
	private GrnService grnService;
	@Autowired
	private InvoiceService invoiceService;

	// Utility Object for invoice
	private List<Customer> allActiveCustomers;
	private List<Stock> stocks;
	private Stock moreDetStockItem;
	private BigDecimal maxFeeDiscount;
	private List<PaymentType> paymentTypes;
	private boolean txtDiscountLock;
	private boolean txtCheqPayLock;
	private boolean txtCashPayLock;

	// New Customer Details
	private String newCusFirstName;
	private String newCusLastName;
	private String newAddress;
	private String newMobile;

	// Invoice Header Objects
	private Customer selectedCustomer;
	private InvHdr invHdr;
	private PaymentType selPayType;
	private CheqType selChequType;
	private Integer invoiceNumber;

	// Invoice Details Object
	private List<InvDet> invDets;
	private int subNumber;
	private InvDet havRemoveDel;

	// Invoice Details Obj
	private Stock selectedItem;
	private BigDecimal qty;
	private BigDecimal discount;

	@PostConstruct
	public void init() {
		LOGGER.info("Execute Invoice Controller Init --------->");
		this.txtDiscountLock = true;
		this.txtCashPayLock = true;
		this.txtCheqPayLock = true;
		loadAllActiveCustomers();
		loadAllStockForPOS();
		this.invHdr = new InvHdr();
		this.invDets = new LinkedList<InvDet>();
		this.subNumber = 1;
		this.invHdr.setTotalAmount(BigDecimal.ZERO);
		this.invHdr.setTransaCharge(BigDecimal.ZERO);
		this.invHdr.setLabourCharge(BigDecimal.ZERO);
		this.invHdr.setTotalDiscoount(BigDecimal.ZERO);
		this.invHdr.setPayblAmount(BigDecimal.ZERO);
		loadPaymentTypes();
	}

	private void loadPaymentTypes() {
		try {
			this.paymentTypes = this.grnService.getPaymentTypes((short) 6);
			if (getPaymentTypes() != null) {
				this.selPayType = getPaymentTypes().get(2);
			}
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

	private void loadAllActiveCustomers() {
		LOGGER.info("<-------- Execute Load All Active Customers In Invoice Controller ------>");
		try {
			this.allActiveCustomers = this.customerService.getAllActiveCustomers();
			if (getAllActiveCustomers() != null) {
				this.selectedCustomer = getAllActiveCustomers().get(0);
			}

		} catch (SocketTimeoutException ste) {
			LOGGER.error("Load All Customer Coldnt Connect to Database Server---", ste);
			addErrorMessage("Load All Active Customers", "Couldnt Connect To Database\n" + ste.getLocalizedMessage());
		} catch (EmptyResultDataAccessException ere) {
			LOGGER.error("Load All Customer Empty Customers---", ere);
			addErrorMessage("Load All Active Customers",
					"Couldnt Find Any Customers On System Admin\n" + ere.getLocalizedMessage());
		} catch (DataAccessException dae) {
			LOGGER.error("Load All CustomerInternal Server Error Please Confirm To System Admin---", dae);
			addErrorMessage("Load All Active Customers",
					"Internal Server Error Please Confirm To System Admin\n" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Load All Customer Internal Server Error Please Confirm To System Admin---", e);
			addErrorMessage("Load All Active Customers",
					"Internal Server Error Please Confirm To System Admin\n" + e.getLocalizedMessage());
		}
	}

	private void loadAllStockForPOS() {
		LOGGER.info("<-------- Execute load All Stock For POS In Invoice Controller ------>");
		try {
			this.stocks = this.stockService.getStockForPOS();

		} catch (SocketTimeoutException ste) {
			LOGGER.error("Load All Stock Items For POS Coldnt Connect to Database Server---", ste);
			addErrorMessage("\"Load All Stock Items For POS",
					"Couldnt Connect To Database\n" + ste.getLocalizedMessage());
		} catch (EmptyResultDataAccessException ere) {
			LOGGER.error("Load All Stock Items For POS Empty Customers---", ere);
			addErrorMessage("Load All Stock Items For POS",
					"Couldnt Find Any Stock Items On System Admin\n" + ere.getLocalizedMessage());
		} catch (DataAccessException dae) {
			LOGGER.error("Load All Stock Items For POS Internal Server Error Please Confirm To System Admin---", dae);
			addErrorMessage("Load All Stock Items For POS",
					"Internal Server Error Please Confirm To System Admin\n" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Load All Stock Items For POS Internal Server Error Please Confirm To System Admin---", e);
			addErrorMessage("Load All Stock Items For POS Customers",
					"Internal Server Error Please Confirm To System Admin\n" + e.getLocalizedMessage());
		}
	}

	public List<Customer> completeCustomerContains(String query) {
		String queryLowerCase = query.toLowerCase();
		return getAllActiveCustomers().stream()
				.filter(c -> c.getFirstName().toLowerCase().contains(queryLowerCase)
						|| c.getLastName().toLowerCase().contains(queryLowerCase)
						|| c.getMobileNumber().toLowerCase().contains(queryLowerCase))
				.collect(Collectors.toList());
	}

	public void onCustomerSelect(SelectEvent<Customer> event) {
		LOGGER.info("Selected Customer: " + event.getObject());
		setSelectedCustomer(event.getObject());
	}

	public List<Stock> completeItemsContains(String query) {
		String queryLowerCase = query.toLowerCase();
		return getStocks().stream()
				.filter(c -> c.getProduct().getPrdCode().toLowerCase().contains(queryLowerCase)
						|| c.getProduct().getPrdName().toLowerCase().contains(queryLowerCase))
				.collect(Collectors.toList());
	}

	public void onItemSelect(SelectEvent<Stock> event) {
		LOGGER.info("Selected Customer: " + event.getObject());
	}

	public void createNewCustomer() {
		LOGGER.info("<----- Execute Create New Customer In Invoice Conttroller -------->");
		try {
			if (getNewCusFirstName().isEmpty()) {
				addErrorMessage("Create New Customer", "Customer First Name Is Required!");
				return;
			}
			if (getNewCusLastName().isEmpty()) {
				addErrorMessage("Create New Customer", "Customer Last Name Is Required!");
				return;
			}
			if (getNewAddress().isEmpty()) {
				addErrorMessage("Create New Customer", "Customer Address Is Required!");
				return;
			}
			if (getNewMobile().isEmpty()) {
				addErrorMessage("Create New Customer", "Customer Mobile Number Is Required!");
				return;
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = new Customer();
			if (auth != null) {
				customer.setCreateBy(auth.getName());
			} else {
				throw new AccessDeniedException("Un Authorized Access !");
			}
			customer.setFirstName(getNewCusFirstName());
			customer.setLastName(getNewCusLastName());
			customer.setAddress(getNewAddress());
			customer.setMobileNumber(getNewMobile());
			customer.setFwBalance(BigDecimal.ZERO);
			customer.setCreateDate(new Date());
			customer.setCustomerState((short) 1);
			if (this.customerService.createNewCustomer(customer) >= 1) {
				addMessage("Create New Customer", "Successfuly Create New Customer!");
				this.newAddress = null;
				this.newCusFirstName = null;
				this.newCusLastName = null;
				this.newMobile = null;
				loadAllActiveCustomers();
			} else {
				addErrorMessage("Create New Customer", "Create New Customer Failed!");
			}
		} catch (Exception e) {
			LOGGER.error("Create New Customer Errr", e);
			addErrorMessage("Create New Customer", "Create New Customer Error!\n" + e.getLocalizedMessage());
		}
	}

	public void getStockItemByNumber() {
		LOGGER.info("<-------- Execute Get Stock Item By Number In Invoice Controller ---->");
		try {
			if (this.getSelectedItem() != null) {
				setMoreDetStockItem(this.stockService.getStockItemByNumber(getSelectedItem().getStockId()));
				if (getMoreDetStockItem() != null) {
					setMaxFeeDiscount(getMaxFeeDiscount());
				}
			}
		} catch (SocketTimeoutException ste) {
			LOGGER.error("Fetch Stock Item By Number Couldnt Connect To Database...", ste);
			addErrorMessage("Fetch Stock Item More Details", "Couldnt Connect Database\n" + ste.getLocalizedMessage());
		} catch (EmptyResultDataAccessException ere) {
			LOGGER.error("Canto Find Fetch Item  ", ere);
			addErrorMessage("Fetch Stock Item More Details", "Couldnt Find Item\n" + ere.getLocalizedMessage());
		} catch (DataAccessException dae) {
			LOGGER.error("Fetch Stock Item By Number Dataaccess Error", dae);
			addErrorMessage("Fetch Stock Item More Details", "Data Access Error\n" + dae.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Fetch Stock Item By Number System Erro", e);
			addErrorMessage("Fetch Stock Item More Details", "System Error Occured\n" + e.getLocalizedMessage());
		}
	}

	private void calcMaxFessDiscount(final Stock stock) {
		BigDecimal maxProfitMargin = BigDecimal.ZERO;
		BigDecimal maxDiscount = BigDecimal.ZERO;
		try {
			maxProfitMargin = stock.getSellingPrice().subtract(stock.getPurchasePrice());
			if (maxProfitMargin.compareTo(BigDecimal.ZERO) != 0) {
				maxDiscount = maxProfitMargin.divide(stock.getPurchasePrice(), 2, RoundingMode.HALF_UP)
						.multiply(new BigDecimal(100.00));
				setMaxFeeDiscount(maxDiscount);

			} else {
				setMaxFeeDiscount(new BigDecimal(0.0));
			}

		} catch (Exception e) {
			LOGGER.error("Calculate Maximumm Feeceble Discount System Error---->", e);
			addErrorMessage("Calculate Maximum Feecable Discount",
					"System Error Occured\nPlease Inform To System Admin\n" + e.getLocalizedMessage());
		}
	}

	public void addTocart() {
		LOGGER.info("Execute Add To cart In Invoice Controller ------->");
		if (getSelectedItem() == null) {
			addErrorMessage("Create New Invoice", "Select Item Is Required !");
			return;
		}
		if (getSelectedItem().getSellingPrice() == null) {
			addErrorMessage("Create New Invoice", "Selling Price Is Required !");
			return;
		}
		if (getQty() == null) {
			addErrorMessage("Create New Invoice", "Quantity  Is Required !");
			return;
		}
		if (getQty().compareTo(BigDecimal.ZERO) <= 0) {
			addErrorMessage("Create New Invoice", "Quantity  Canot be Zero Or minus !");
			return;
		}
		try {
			InvDet checkInvDet = chechDuplicateInvDetItem();
			if (checkInvDet != null) {
				getInvHdr().setTotalAmount(getInvHdr().getTotalAmount().subtract(checkInvDet.getAmount()));
				checkInvDet.setQty(checkInvDet.getQty().add(getQty()));
				if (checkInvDet.getDiscount() == null) {
					checkInvDet.setAmount(checkInvDet.getSellingPrice().multiply(checkInvDet.getQty()));
				} else if (checkInvDet.getDiscount().compareTo(BigDecimal.ZERO) == 0) {
					checkInvDet.setAmount(checkInvDet.getSellingPrice().multiply(checkInvDet.getQty()));
				} else {
					BigDecimal discountRate = checkInvDet.getDiscount().divide(new BigDecimal("100")).setScale(2,
							RoundingMode.HALF_EVEN);
					BigDecimal discountValue = checkInvDet.getSellingPrice().multiply(checkInvDet.getQty())
							.multiply(discountRate);
					checkInvDet.setAmount(
							checkInvDet.getSellingPrice().multiply(checkInvDet.getQty()).subtract(discountValue));
				}
				setFooterValue(checkInvDet, 1);
				addMessage("Add New Invoice Item", "Successfuly Add New Invoice Item!");
				setTxtDiscountLock(false);
				setTxtCashPayLock(false);
				clearInvDet(1);
			} else {
				BigDecimal discountVal = BigDecimal.ZERO;
				BigDecimal cusAdva = BigDecimal.ZERO;

				InvDet invDet = new InvDet();
				invDet.setInvDetNumber(subNumber);
				invDet.setBatchNumber(getSelectedItem().getBatchNumber());
				invDet.setStockItem(getSelectedItem());
				invDet.setPurchacePrice(getSelectedItem().getPurchasePrice());
				invDet.setSellingPrice(getSelectedItem().getSellingPrice());
				invDet.setQty(getQty());
				if (getDiscount() == null || getDiscount().compareTo(BigDecimal.ZERO) == 0) {
					invDet.setDiscount(discountVal);
					invDet.setCusAdva(cusAdva);
					invDet.setAmount(getSelectedItem().getSellingPrice().multiply(getQty()).setScale(2));
				} else {
					if (getMaxFeeDiscount() != null) {
						if (getDiscount().compareTo(getMaxFeeDiscount()) == 1)
							addWarMessage("Add New Stock Items",
									"The Max Feesable Discount Is Exceed\nThis Item Generate Loss Profit Margin !");

					}
					discountVal = getSelectedItem().getSellingPrice()
							.multiply(getDiscount().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_EVEN);
					invDet.setDiscount(getDiscount());
					cusAdva = getSelectedItem().getSellingPrice().subtract(discountVal);
					invDet.setAmount(
							getSelectedItem().getSellingPrice().subtract(discountVal).multiply(getQty()).setScale(2));

				}
				invDet.setInvHdr(getInvHdr());

				if (getInvDets().add(invDet)) {
					setFooterValue(invDet, 1);
					addMessage("Add New Invoice Item", "Successfuly Add New Invoice Item!");
					setTxtDiscountLock(false);
					setTxtCashPayLock(false);
					clearInvDet(0);
				} else {
					addErrorMessage("Add New Invoice Item", "Add New Invocie Item Failed Try Again!");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Create New Invoice Add To Cart error", e);
			addErrorMessage("Add New Invoice Item",
					"System Error Occured Please Contact With System Admin\n" + e.getLocalizedMessage());
		}
	}

	private InvDet chechDuplicateInvDetItem() {
		LOGGER.info("<-------- Execute Check Duplicate Invoice Item ----->");
		if (getInvDets().size() > 0) {
			return getInvDets().stream().filter(
					i -> i.getStockItem().getProduct().getPrdId().equals(getSelectedItem().getProduct().getPrdId())
							&& i.getStockItem().getBatchNumber().equals(getSelectedItem().getBatchNumber()))
					.findAny().orElse(null);
		}
		return null;
	}

	private void removeFromCart() {
		LOGGER.info("<------ Execute Remove Invoice Item From Cart --->");
		try {
			if (getInvDets().remove(getHavRemoveDel())) {
				if (getInvDets().size() <= 0) {
					setTxtDiscountLock(true);
					setTxtCashPayLock(true);
				}
				setFooterValue(getHavRemoveDel(), 0);
				addMessage("Remove Invoice Item", "Successfuly Remove Invoice Item!");
			} else {
				addErrorMessage("Remove Invoice Item", "Remove Invoice Iteme Failed!");
			}
		} catch (Exception e) {
			LOGGER.error("Remove Invoice Item From Cart Error-->", e);
			addErrorMessage("Remove Invoice Item", "System Error Occured!\n" + e.getLocalizedMessage());
		}
	}

	private void setFooterValue(final InvDet invDet, final int state) throws Exception {
		LOGGER.info("<------- Execute Invoice Header Set Footer Value ------>");
		switch (state) {
		case 1:
			getInvHdr().setTotalAmount(getInvHdr().getTotalAmount().add(invDet.getAmount()));
			getInvHdr().setPayblAmount(invHdr.getTotalAmount());
			break;
		case 0:
			getInvHdr().setTotalAmount(getInvHdr().getTotalAmount().subtract(invDet.getAmount()));
			getInvHdr().setPayblAmount(invHdr.getTotalAmount());
			break;
		}

	}

	public void addTransportCost() {
		LOGGER.info("<------ Execute Add  Transport  Cost --->");
		if (getInvDets().size() <= 0) {
			addErrorMessage("Add Transportation Charge", "Atleast One Invoice Item Is Required!");
			getInvHdr().setTransaCharge(BigDecimal.ZERO);
			return;
		}
		if (getInvHdr().getTransaCharge() == null) {
			addErrorMessage("Add Transportation Charge", "Labour Cost Is Required!");
			return;
		}
		if (getInvHdr().getTransaCharge().compareTo(BigDecimal.ZERO) <= 0) {
			addErrorMessage("Add Transportation Charge", "Transportaion Charge Canot be Zero Or minus !");
			return;
		}
		try {
			getInvHdr().setPayblAmount(getInvHdr().getPayblAmount().add(getInvHdr().getTransaCharge()));
			addMessage("Add Transportation Charge", "Successfuly Add Transportation Charge!");
		} catch (Exception e) {
			LOGGER.error("Add Transportation Charge Error", e);
			addErrorMessage("Add Transportation Charge",
					"Add Transportation Charge Failed Error Occured\n" + e.getLocalizedMessage());
		}
	}

	public void addLabourCost() {
		LOGGER.info("<------ Execute Add Labour  Cost --->");
		if (getInvDets().size() <= 0) {
			addErrorMessage("Add Labour Cost", "Atleast One Invoice Item Is Required!");
			getInvHdr().setLabourCharge(BigDecimal.ZERO);
			return;
		}
		if (getInvHdr().getLabourCharge() == null) {
			addErrorMessage("Add Labour Cost", "Labour Cost Is Required!");
			return;
		}
		if (getInvHdr().getLabourCharge().compareTo(BigDecimal.ZERO) <= 0) {
			addErrorMessage("Add Labour Cost", "Labour Cost  Canot be Zero Or minus !");
			return;
		}
		try {
			getInvHdr().setPayblAmount(getInvHdr().getPayblAmount().add(getInvHdr().getLabourCharge()));
			addMessage("Add Labour Cost", "Successfuly Add Labour Cost!");
		} catch (Exception e) {
			LOGGER.error("Add Labour Cost Error", e);
			addErrorMessage("Add Labour Cost", "Add Labour Cost Failed Error Occured\n" + e.getLocalizedMessage());
		}
	}

	public void handleDiscountKeyEvent(ValueChangeEvent evt) {

		BigDecimal discounValue = (BigDecimal) evt.getNewValue();
		BigDecimal initPayble = new BigDecimal(getInvHdr().getTotalAmount().doubleValue()).add(invHdr.getLabourCharge())
				.add(invHdr.getTransaCharge());
		try {
			if (discounValue == null) {
				getInvHdr().setPayblAmount(initPayble);
				getInvHdr().setTotalDiscoount(BigDecimal.ZERO);
			} else if (discounValue.compareTo(BigDecimal.ZERO) == 0) {
				getInvHdr().setPayblAmount(initPayble);
				getInvHdr().setTotalDiscoount(BigDecimal.ZERO);
			} else {
				getInvHdr().setTotalDiscoount(discounValue);
				getInvHdr().setPayblAmount(getInvHdr().getPayblAmount().subtract(getInvHdr().getTotalAmount()
						.multiply(discounValue.divide(BigDecimal.valueOf(100.00), 2, RoundingMode.HALF_EVEN))));
			}
		} catch (NumberFormatException nfe) {
			LOGGER.error("Enter Discount Invalied Input", nfe);
			addMessage("Enter Discount", "Invalied Input\n" + nfe.getLocalizedMessage());
		} catch (ArithmeticException ame) {
			LOGGER.error("Enter Discount Arithmetic Exception", ame);
			addMessage("Enter Discount", "Arithmetic Exception\n" + ame.getLocalizedMessage());
		} catch (NullPointerException npe) {
			getInvHdr().setPayblAmount(initPayble);
			getInvHdr().setTotalDiscoount(BigDecimal.ZERO);
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
					// TODO Invoice Credit Payment Goes Hear
					break;

				case TeleiosPosConstant.CASH:
					// TODO Invoice Cash Payment Goes Hear
					setTxtDiscountLock(false);
					setTxtCashPayLock(false);
					break;

				case TeleiosPosConstant.CASHANDCHEQUE:
					addErrorMessage("Select Payment Type", "Operation Notyet Added!");
					break;

				case TeleiosPosConstant.CASHANDCREDI:
					addErrorMessage("Select Payment Type", "Operation Notyet Added!");
					// TODO Invoice Cash & Credit Payment Goes Hear
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

	public void setCashReceved() {
		LOGGER.info("<----- Execute Set Cash Receved ----->");
		try {
			if (getSelPayType() != null) {
				if (invHdr.getCashValue() == null) {
					addErrorMessage("Enter Cash Receved", "The Cash Value Is Required!");
					return;
				}
				if (invHdr.getCashValue().compareTo(BigDecimal.ZERO) <= 0) {
					addErrorMessage("Enter Cash Receved", "The Cash Payment Shuld Gretter thaan Zero!");
					return;
				}
				if (invHdr.getCashValue().compareTo(invHdr.getPayblAmount()) == -1) {
					addErrorMessage("Couldnd Complite Cash Invoice",
							"The Cash Payment Shuld Gretter thaan Or Equal to Invoice Payble Amount!");
					return;
				}

				if (getSelPayType().getPayTypeId() == TeleiosPosConstant.CASH) {
					invHdr.setTotalPaid(getInvHdr().getPayblAmount());
					invHdr.setBalance(getInvHdr().getPayblAmount().subtract(invHdr.getCashValue()));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Handle Cashier Cashe Recevd Error", e);
			addErrorMessage("Enter Cash Recved", "System Error Ocured\n" + e.getLocalizedMessage());
		}
	}

	public void onCashChange(ValueChangeEvent evt) {
		BigDecimal paidValue = (BigDecimal) evt.getNewValue();
		try {
			if (paidValue == null) {
				getInvHdr().setBalance(getInvHdr().getPayblAmount());
				getInvHdr().setTotalPaid(BigDecimal.ZERO);
			} else if (paidValue.compareTo(BigDecimal.ZERO) == 0) {
				getInvHdr().setCashValue(BigDecimal.ZERO);
				getInvHdr().setBalance(getInvHdr().getPayblAmount());
			} else {
				getInvHdr().setBalance(getInvHdr().getPayblAmount().subtract(paidValue));
				getInvHdr().setTotalPaid(paidValue);
			}
		} catch (NumberFormatException nfe) {
			LOGGER.error("Enter Cash Recived Invalied Input", nfe);
			addMessage("Enter Cash Recived", "Invalied Input\n" + nfe.getLocalizedMessage());
		} catch (ArithmeticException ame) {
			LOGGER.error("Enter Cash Recived Arithmetic Exception", ame);
			addMessage("Enter Cash Recived", "Arithmetic Exception\n" + ame.getLocalizedMessage());
		} catch (NullPointerException npe) {
			getInvHdr().setCashValue(BigDecimal.ZERO);
			getInvHdr().setBalance(getInvHdr().getPayblAmount());
			getInvHdr().setTotalPaid(BigDecimal.ZERO);
		} catch (Exception e) {
			LOGGER.error("Cash Recived System Error Occured", e);
			addMessage("Enter Cash Recived", "System Error Occured\n" + e.getLocalizedMessage());
		}
	}

	public void confirmInvoice() {
		LOGGER.info("<-------- Execute Confirm Invoice --------->");
		if (getInvDets().size() <= 0) {
			addErrorMessage("Confirm Invoice", "Atlease One Item Required !");
			return;
		}
		if (getSelPayType() == null) {
			addErrorMessage("Confirm Invoice", "Select Payment Type Is Required !");
			return;
		}
		if (getSelectedCustomer() == null) {
			addErrorMessage("Confirm Invoice", "Select Customer Is Required !");
			return;
		}
		try {

			switch (getSelPayType().getPayTypeId()) {
			case TeleiosPosConstant.CHEQUE:
				addErrorMessage("Confirm Invoice", "Operation Notyet Added!");
				break;

			case TeleiosPosConstant.CREDICT:
				// TODO Invoice Credit Payment Goes Hear
				break;

			case TeleiosPosConstant.CASH:
				try {
					if (invHdr.getCashValue() == null) {
						addErrorMessage("Couldnd Complite Cash Invoice", "The Cash Value Is Required!");
						return;
					}
					if (getInvHdr().getTotalPaid() == null) {
						addErrorMessage("Confirm Invoice", "The Paid Amount Is Required!");
						return;
					}
					if (invHdr.getTotalPaid().compareTo(invHdr.getPayblAmount()) == -1) {
						addErrorMessage("Couldnd Complite Cash Invoice",
								"The Cash Payment Shuld Gretter thaan Or Equal to Invoice Payble Amount!");
						return;
					}
					getInvHdr().setCreateDate(new Date());
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					if (auth != null) {
						getInvHdr().setCreateBy(auth.getName());
					} else {
						throw new AccessDeniedException("Un Authorized Access !");
					}
					getInvHdr().setState(TeleiosPosConstant.CASH);
					getInvHdr().setCustomer(getSelectedCustomer());
					getInvHdr().setPayType(getSelPayType());
					getInvHdr().setChequAmount(BigDecimal.ZERO);
					getInvHdr().setBarcode("123456");

					CashRecived cashRecived = new CashRecived(new Integer(1), getInvHdr().getPayblAmount(),
							getInvHdr().getCashValue(), getInvHdr().getBalance(), getInvHdr().getCreateDate(), null,
							auth.getName(), (short) 1, getSelectedCustomer(), getInvHdr());
					getInvHdr().setBalance(BigDecimal.ZERO);

					Integer createdInvoiceNumber = this.invoiceService.createNewCashInvoice(getInvHdr(), getInvDets(),
							cashRecived);

					if (createdInvoiceNumber != null) {
						setInvoiceNumber(createdInvoiceNumber);
						addMessage("Confirm Cash Invoice",
								"Successfuly Created Inovoice!\nInvoice Number: " + getInvoiceNumber());
						clearAll();
						// TODO PRINT INVOICE GOES HEAR
					} else {
						addErrorMessage("Confirm Cash Invoice", "Invoice Create Failed !");
					}
				} catch (SocketTimeoutException ste) {
					LOGGER.error("Confirm New Cash Invoice Coluldnt Connect To Database Error", ste);
					addErrorMessage("Confirm New Cash Invoice",
							"Coluldnt Connecto To Data Base Please Inform To System Admin\n"
									+ ste.getLocalizedMessage());
				} catch (DataAccessException dae) {
					LOGGER.error("Confirm New Cash Invoice Data Access Error", dae);
					addErrorMessage("Confirm New Cash Invoice",
							"Data Access Errro Please Inform To System Admin\n" + dae.getLocalizedMessage());
				} catch (Exception e) {
					LOGGER.error("Confirm New Cash Invoice System  Error Occured", e);
					addErrorMessage("Confirm New Cash Invoice",
							"System  Error Occured Please Inform To System Admin\n" + e.getLocalizedMessage());
				}
				break;

			case TeleiosPosConstant.CASHANDCHEQUE:
				addErrorMessage("Confirm Invoice", "Operation Notyet Added!");
				break;

			case TeleiosPosConstant.CASHANDCREDI:
				addErrorMessage("Select Payment Type", "Operation Notyet Added!");
				// TODO Invoice Cash & Credit Payment Goes Hear
				break;

			default:
				addErrorMessage("Confirm Invoice", "Invalied Payment Type!");
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void clearInvDet(final int flag) {
		LOGGER.info("<-------- Clear Invoice Details Execute -------->");
		try {
			this.qty = null;
			this.discount = null;
			this.selectedItem = null;
			if (flag == 0) {
				this.subNumber++;
			}
		} catch (Exception e) {
			LOGGER.error("Clear Invoice Details Error--->", e);
			addErrorMessage("Add New Invoice Item\nClear Old Invoice Item",
					"System Error Occured\n" + e.getLocalizedMessage());
		}
	}

	private void clearAll() {
		LOGGER.info("<------- Execute Clear All In Invoice Controller --------->");
		try {
			this.invDets.clear();
			this.invHdr = null;
			this.txtDiscountLock = true;
			this.txtCashPayLock = true;
			this.txtCheqPayLock = true;
			this.invHdr = new InvHdr();
			this.subNumber = 1;
			this.invHdr.setTotalAmount(BigDecimal.ZERO);
			this.invHdr.setTransaCharge(BigDecimal.ZERO);
			this.invHdr.setLabourCharge(BigDecimal.ZERO);
			this.invHdr.setTotalDiscoount(BigDecimal.ZERO);
			this.invHdr.setPayblAmount(BigDecimal.ZERO);
			this.selPayType = getPaymentTypes().get(2);
			this.selectedCustomer = getAllActiveCustomers().get(0);
			this.selectedItem = null;
		} catch (Exception e) {
			LOGGER.error("Clear All Error", e);
			addErrorMessage("Confirm Invoice ", "Clear All Previus Invoice Details Errror\n" + e.getLocalizedMessage());
		}
	}

	// Faces Messages
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

	public List<Customer> getAllActiveCustomers() {
		return allActiveCustomers;
	}

	public void setAllActiveCustomers(List<Customer> allActiveCustomers) {
		this.allActiveCustomers = allActiveCustomers;
	}

	public Customer getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(Customer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public String getNewCusFirstName() {
		return newCusFirstName;
	}

	public void setNewCusFirstName(String newCusFirstName) {
		this.newCusFirstName = newCusFirstName;
	}

	public String getNewCusLastName() {
		return newCusLastName;
	}

	public void setNewCusLastName(String newCusLastName) {
		this.newCusLastName = newCusLastName;
	}

	public String getNewAddress() {
		return newAddress;
	}

	public void setNewAddress(String newAddress) {
		this.newAddress = newAddress;
	}

	public String getNewMobile() {
		return newMobile;
	}

	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public Stock getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(Stock selectedItem) {
		this.selectedItem = selectedItem;
		calcMaxFessDiscount(selectedItem);
	}

	public Stock getMoreDetStockItem() {
		return moreDetStockItem;
	}

	public void setMoreDetStockItem(Stock moreDetStockItem) {
		this.moreDetStockItem = moreDetStockItem;
	}

	public BigDecimal getMaxFeeDiscount() {
		return maxFeeDiscount;
	}

	public void setMaxFeeDiscount(BigDecimal maxFeeDiscount) {
		this.maxFeeDiscount = maxFeeDiscount;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public InvHdr getInvHdr() {
		return invHdr;
	}

	public void setInvHdr(InvHdr invHdr) {
		this.invHdr = invHdr;
	}

	public List<InvDet> getInvDets() {
		return invDets;
	}

	public void setInvDets(List<InvDet> invDets) {
		this.invDets = invDets;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public InvDet getHavRemoveDel() {
		return havRemoveDel;
	}

	public void setHavRemoveDel(InvDet havRemoveDel) {
		this.havRemoveDel = havRemoveDel;
		removeFromCart();
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

	public List<PaymentType> getPaymentTypes() {
		return paymentTypes;
	}

	public void setPaymentTypes(List<PaymentType> paymentTypes) {
		this.paymentTypes = paymentTypes;
	}

	public boolean isTxtCheqPayLock() {
		return txtCheqPayLock;
	}

	public void setTxtCheqPayLock(boolean txtCheqPayLock) {
		this.txtCheqPayLock = txtCheqPayLock;
	}

	public boolean isTxtCashPayLock() {
		return txtCashPayLock;
	}

	public void setTxtCashPayLock(boolean txtCashPayLock) {
		this.txtCashPayLock = txtCashPayLock;
	}

	public boolean isTxtDiscountLock() {
		return txtDiscountLock;
	}

	public void setTxtDiscountLock(boolean txtDiscountLock) {
		this.txtDiscountLock = txtDiscountLock;
	}

	public Integer getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Integer invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

}
