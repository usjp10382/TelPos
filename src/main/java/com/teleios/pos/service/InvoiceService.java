package com.teleios.pos.service;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.teleios.pos.dao.impl.CashRecivedDaoImpl;
import com.teleios.pos.dao.impl.CustomerDaoImpl;
import com.teleios.pos.dao.impl.InvoiceDaoImpl;
import com.teleios.pos.dao.impl.RecevableDaoImpl;
import com.teleios.pos.dao.impl.StockDaoImpl;
import com.teleios.pos.model.CashRecived;
import com.teleios.pos.model.InvDet;
import com.teleios.pos.model.InvHdr;
import com.teleios.pos.model.Receivable;

@Service
public class InvoiceService implements Serializable {
	private static final long serialVersionUID = -527249198857324233L;
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);

	private InvoiceDaoImpl invoiceDaoImpl;
	private StockDaoImpl stockDaoImpl;
	private CashRecivedDaoImpl cashRecivedDaoImpl;
	private CustomerDaoImpl customerDaoImpl;
	private RecevableDaoImpl recevableDaoImpl;

	@Autowired
	public InvoiceService(InvoiceDaoImpl invoiceDaoImpl, StockDaoImpl stockDaoImpl,
			CashRecivedDaoImpl cashRecivedDaoImpl, CustomerDaoImpl customerDaoImpl, RecevableDaoImpl recevableDaoImpl) {
		this.invoiceDaoImpl = invoiceDaoImpl;
		this.stockDaoImpl = stockDaoImpl;
		this.cashRecivedDaoImpl = cashRecivedDaoImpl;
		this.customerDaoImpl = customerDaoImpl;
		this.recevableDaoImpl = recevableDaoImpl;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, timeout = 100)
	public Integer createNewCashInvoice(InvHdr invHdr, List<InvDet> invDets, CashRecived cashRecived)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("******* Executing Create New Cash Invoice  In Invoice Service **********");

		Integer invoiceNumber = null;
		invoiceNumber = this.invoiceDaoImpl.createNewInvoiceHeader(invHdr);
		this.invoiceDaoImpl.createNewInvoiceDetails(invDets, invoiceNumber);
		this.stockDaoImpl.updateStockForNewInvoice(invDets);
		this.cashRecivedDaoImpl.createNewCashRecived(cashRecived, invoiceNumber);

		return invoiceNumber;

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, timeout = 100)
	public Integer createNewCreditInvoice(InvHdr invHdr, List<InvDet> invDets, Receivable receivable)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("******* Executing Create New Credit Invoice  In Invoice Service **********");

		Integer invoiceNumber = null;
		invoiceNumber = this.invoiceDaoImpl.createNewInvoiceHeader(invHdr);
		this.invoiceDaoImpl.createNewInvoiceDetails(invDets, invoiceNumber);
		this.stockDaoImpl.updateStockForNewInvoice(invDets);
		this.recevableDaoImpl.createNewRecevable(receivable);
		this.customerDaoImpl.updateCustomerBalance(invHdr.getCustomer(), invHdr.getBalance());

		return invoiceNumber;

	}

}
