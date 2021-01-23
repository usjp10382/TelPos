package com.teleios.pos.dao;

import java.net.SocketTimeoutException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.InvDet;
import com.teleios.pos.model.InvHdr;

public interface InvoiceDao {
	Integer createNewInvoiceHeader(InvHdr invHdr) throws SocketTimeoutException, DataAccessException, Exception;

	int[] createNewInvoiceDetails(List<InvDet> invDets, Integer invNumber)
			throws SocketTimeoutException, DataAccessException, Exception;

	Integer getNextInvoiceNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;
}
