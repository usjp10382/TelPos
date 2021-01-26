package com.teleios.pos.dao;

import java.net.SocketTimeoutException;

import org.springframework.dao.DataAccessException;

import com.teleios.pos.model.CashPayment;
import com.teleios.pos.model.CheqDetails;
import com.teleios.pos.model.Payeble;

public interface PaymentDao {
	int createNewCashPaymment(CashPayment cashPayment) throws SocketTimeoutException, DataAccessException, Exception;

	int createNewCheqePaymment(CheqDetails cheqDetails) throws SocketTimeoutException, DataAccessException, Exception;

	int createNewPayble(Payeble payeble) throws SocketTimeoutException, DataAccessException, Exception;
}
