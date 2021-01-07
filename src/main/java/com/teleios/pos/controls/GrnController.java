package com.teleios.pos.controls;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.CheqType;
import com.teleios.pos.model.PaymentType;
import com.teleios.pos.model.Product;
import com.teleios.pos.model.Supplier;
import com.teleios.pos.service.ProductService;
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

	// Utility Object
	private List<PaymentType> paymentTypes;
	private List<CheqType> cheqTypes;

	// Grn Header Related Selected Objects
	private Supplier selSuppliyer;
	private PaymentType selPayType;
	private CheqType selChequType;

	// Required Helper List Of Objects
	private List<Product> allActiveProducts;
	private List<Supplier> allActiveSuppliyers;

	@Autowired
	public void init() {
		LOGGER.info("<--- Inizilalising Grn Controller ........");

		this.paymentTypes = new ArrayList<PaymentType>();

		PaymentType chequType = new PaymentType((short) 1, "Cheque", "Teleios", new Date(), (short) 1);
		paymentTypes.add(chequType);

		PaymentType creditType = new PaymentType((short) 2, "Credit", "Teleios", new Date(), (short) 1);
		paymentTypes.add(creditType);

		PaymentType cashType = new PaymentType((short) 3, "Cash", "Teleios", new Date(), (short) 1);
		paymentTypes.add(cashType);

		PaymentType fullType = new PaymentType((short) 4, "Full", "Teleios", new Date(), (short) 1);
		paymentTypes.add(fullType);

		PaymentType harlfType = new PaymentType((short) 5, "Half", "Teleios", new Date(), (short) 1);
		paymentTypes.add(harlfType);

		loadAllActiveProducts();
		loadAllActiveSuppliyers();
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

}
