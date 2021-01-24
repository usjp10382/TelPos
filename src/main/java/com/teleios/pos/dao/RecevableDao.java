package com.teleios.pos.dao;

import java.net.SocketTimeoutException;

import org.springframework.dao.DataAccessException;

import com.teleios.pos.model.Receivable;

public interface RecevableDao {
	int createNewRecevable(Receivable receivable) throws SocketTimeoutException, DataAccessException, Exception;
}
