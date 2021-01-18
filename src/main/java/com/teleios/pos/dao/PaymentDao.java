package com.teleios.pos.dao;

import java.net.SocketTimeoutException;

import org.springframework.dao.DataAccessException;

import com.teleios.pos.model.CashPayment;

public interface PaymentDao {
	int createNewCashPaymment(CashPayment cashPayment) throws SocketTimeoutException, DataAccessException, Exception;
}
