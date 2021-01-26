package com.teleios.pos.service;

import java.net.SocketTimeoutException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.teleios.pos.dao.impl.GrnDaoImpl;
import com.teleios.pos.dao.impl.PaymentDaoImpl;
import com.teleios.pos.dao.impl.StockDaoImpl;
import com.teleios.pos.model.CashPayment;
import com.teleios.pos.model.CheqDetails;
import com.teleios.pos.model.GrnHdr;
import com.teleios.pos.model.Payeble;
import com.teleios.pos.model.PaymentType;

@Service
public class GrnService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GrnService.class);

	private GrnDaoImpl grnDaoImpl;
	private StockDaoImpl stockDaoImpl;
	private PaymentDaoImpl paymentDaoImpl;

	@Autowired
	public GrnService(GrnDaoImpl grnDaoImpl, StockDaoImpl stockDaoImpl, PaymentDaoImpl paymentDaoImpl) {
		super();
		this.grnDaoImpl = grnDaoImpl;
		this.stockDaoImpl = stockDaoImpl;
		this.paymentDaoImpl = paymentDaoImpl;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, timeout = 100)
	public int[] createNewChequPayGrn(GrnHdr grnHdr, CheqDetails cheqDetails)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("******* Executing Create New Chequ Payment Grn In Service **********");
		int[] saveStates = null;

		this.grnDaoImpl.createNewGrnHeder(grnHdr);
		this.grnDaoImpl.createNewGrnDetails(grnHdr.getGrnDets());
		this.paymentDaoImpl.createNewCheqePaymment(cheqDetails);
		saveStates = this.stockDaoImpl.createNewStockItems(grnHdr.getGrnDets(), grnHdr.getBatchNumber());

		return saveStates;

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, timeout = 100)
	public int[] createNewCreditPayGrn(GrnHdr grnHdr, Payeble payeble)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("******* Executing Create New Credit Payment Grn In Service **********");
		int[] saveStates = null;

		this.grnDaoImpl.createNewGrnHeder(grnHdr);
		this.grnDaoImpl.createNewGrnDetails(grnHdr.getGrnDets());
		this.paymentDaoImpl.createNewPayble(payeble);
		saveStates = this.stockDaoImpl.createNewStockItems(grnHdr.getGrnDets(), grnHdr.getBatchNumber());

		return saveStates;

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, timeout = 100)
	public int[] createNewCashPayGrn(GrnHdr grnHdr, CashPayment cashPayment)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("******* Executing Create New Cash Payment Grn In Service **********");
		int[] saveStates = null;

		this.grnDaoImpl.createNewGrnHeder(grnHdr);
		this.grnDaoImpl.createNewGrnDetails(grnHdr.getGrnDets());
		this.paymentDaoImpl.createNewCashPaymment(cashPayment);
		saveStates = this.stockDaoImpl.createNewStockItems(grnHdr.getGrnDets(), grnHdr.getBatchNumber());

		return saveStates;

	}

	public List<PaymentType> getPaymentTypes(short maxRes)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<--- Execute Geting Payment Types For Max:{} In Service--->", maxRes);
		return this.grnDaoImpl.getPaymentTypes(maxRes);
	}

	public Integer getNextBatchNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<-- Fetching Next Grn Number In Service ---->");
		Integer nextGrnNum = null;
		try {
			nextGrnNum = this.grnDaoImpl.getNextGrnNumber() + 1;
		} catch (NullPointerException npe) {
			nextGrnNum = Integer.valueOf(1);
		}
		return nextGrnNum;
	}

	public Integer getNextGrnNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<-- Fetching Next Batch Number In Service ---->");
		Integer nextBatchNum = null;
		try {
			nextBatchNum = this.grnDaoImpl.getNextBatchNumber() + 1;
		} catch (NullPointerException npe) {
			nextBatchNum = Integer.valueOf(1);
		}
		return nextBatchNum;
	}

}
