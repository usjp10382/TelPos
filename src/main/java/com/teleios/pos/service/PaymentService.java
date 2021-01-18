package com.teleios.pos.service;

import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.teleios.pos.dao.PaymentDao;
import com.teleios.pos.dao.impl.PaymentDaoImpl;
import com.teleios.pos.model.CashPayment;

@Service
public class PaymentService implements PaymentDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
	private PaymentDaoImpl paymentDaoImpl;

	@Autowired
	public PaymentService(PaymentDaoImpl paymentDaoImpl) {
		this.paymentDaoImpl = paymentDaoImpl;
	}

	@Override
	public int createNewCashPaymment(CashPayment cashPayment)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("Executing Create New Cash Payment in service -------->");
		return paymentDaoImpl.createNewCashPaymment(cashPayment);
	}

}
