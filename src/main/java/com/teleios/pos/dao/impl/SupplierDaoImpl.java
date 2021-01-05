package com.teleios.pos.dao.impl;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.SupplierDao;
import com.teleios.pos.model.Supplier;

@Repository
public class SupplierDaoImpl implements Serializable, SupplierDao {
	private static final long serialVersionUID = 8847430318501553663L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SupplierDaoImpl.class);

	// Define Supplier Create SQL
	private static final String CRET_SUPP_SQL = "INSERT INTO supplier_schema.supplier (supp_name,address,contact_number,create_by,create_date,spu_state,fix_contact,email)"
			+ " VALUES(:supp_name,:address,:contact_number,:create_by,:create_date,:spu_state,:fix_contact,:email)";

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public SupplierDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public int createNewSuppliyer(Supplier supplier) throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("Execute Create New Suppliyer Supp_Name: {} In Repository ------>", supplier.getSupplierName());
		int saveState = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();

		paraMap.put("supp_name", supplier.getSupplierName());
		paraMap.put("address", supplier.getAddress());
		paraMap.put("contact_number", supplier.getContactNumber());
		paraMap.put("create_by", supplier.getCreateBy());
		paraMap.put("create_date", supplier.getCreateDate());
		paraMap.put("spu_state", supplier.getSuppState());
		paraMap.put("fix_contact", supplier.getFixedNumber());
		paraMap.put("email", supplier.getEmail());

		saveState = this.namedParameterJdbcTemplate.update(CRET_SUPP_SQL, paraMap);

		return saveState;
	}

	@Override
	public int[] createNewSuppliyer(List<Supplier> supplier)
			throws SocketTimeoutException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateSuppliyer(Supplier supplier) throws SocketTimeoutException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteSuppliyer(Supplier supplier) throws SocketTimeoutException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Supplier getSuppliyerById(Integer suppId)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Supplier> getAllActiveSuppliyer(Supplier supplier)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Supplier> getAllSuppliyer(Supplier supplier)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
