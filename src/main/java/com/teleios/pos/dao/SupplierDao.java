package com.teleios.pos.dao;

import java.net.SocketTimeoutException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.Supplier;

public interface SupplierDao {
	int createNewSuppliyer(Supplier supplier) throws SocketTimeoutException, DataAccessException, Exception;

	int[] createNewSuppliyer(List<Supplier> supplier) throws SocketTimeoutException, DataAccessException, Exception;

	int updateSuppliyer(Supplier supplier) throws SocketTimeoutException, DataAccessException, Exception;

	int deleteSuppliyer(Supplier supplier) throws SocketTimeoutException, DataAccessException, Exception;

	Supplier getSuppliyerById(final Integer suppId)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;

	List<Supplier> getAllActiveSuppliyer(Supplier supplier)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;

	List<Supplier> getAllSuppliyer(Supplier supplier)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;
}
