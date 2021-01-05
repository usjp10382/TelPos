package com.teleios.pos.service;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.teleios.pos.dao.impl.SupplierDaoImpl;
import com.teleios.pos.model.Supplier;

@Service
public class SupplierService implements Serializable {

	private static final long serialVersionUID = -3825519826925671189L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SupplierService.class);

	private SupplierDaoImpl supplierDaoImpl;

	@Autowired
	public SupplierService(SupplierDaoImpl supplierDaoImpl) {
		this.supplierDaoImpl = supplierDaoImpl;
	}

	public int createNewSuppliyer(Supplier supplier) throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("Execute Create New Suppliyer Supp_Name: {} In Service ------>", supplier.getSupplierName());
		return this.supplierDaoImpl.createNewSuppliyer(supplier);
	}

	public int[] createNewSuppliyer(List<Supplier> supplier)
			throws SocketTimeoutException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public int updateSuppliyer(Supplier supplier) throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<----- Execute Update Suppliyer Name: {} In Service ----> ", supplier.getSupplierName());
		return this.supplierDaoImpl.updateSuppliyer(supplier);
	}

	public int deleteSuppliyer(Supplier supplier) throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<----- Execute Delete Suppliyer Name: {} In Service ----> ", supplier.getSupplierName());
		return this.supplierDaoImpl.deleteSuppliyer(supplier);
	}

	public Supplier getSuppliyerById(Integer suppId)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Supplier> getAllActiveSuppliyer(final short suppStateNumber)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<----- Execute Get All Active Suppliyers in Service ---->");
		return this.supplierDaoImpl.getAllActiveSuppliyer(suppStateNumber);
	}

	public List<Supplier> getAllSuppliyer(Supplier supplier)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
