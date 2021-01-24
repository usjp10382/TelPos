package com.teleios.pos.dao;

import java.net.SocketTimeoutException;

import org.springframework.dao.DataAccessException;

import com.teleios.pos.model.CashRecived;

public interface CashRecivedDao {
	int createNewCashRecived(CashRecived cashRecived,Integer invNumber) throws SocketTimeoutException, DataAccessException, Exception;
}
