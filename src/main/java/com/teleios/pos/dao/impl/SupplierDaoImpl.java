package com.teleios.pos.dao.impl;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.SupplierDao;
import com.teleios.pos.dao.utill.SuppResExecutor;
import com.teleios.pos.model.Supplier;

@Repository
public class SupplierDaoImpl implements Serializable, SupplierDao {
	private static final long serialVersionUID = 8847430318501553663L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SupplierDaoImpl.class);

	// Define Supplier Create SQL
	private static final String CRET_SUPP_SQL = "INSERT INTO supplier_schema.supplier (supp_name,address,contact_number,create_by,create_date,spu_state,fix_contact,email)"
			+ " VALUES(:supp_name,:address,:contact_number,:create_by,:create_date,:spu_state,:fix_contact,:email)";
	private static final String CRE_BAT_SUPP_SQL = "INSERT INTO supplier_schema.supplier (supp_name,address,contact_number,create_by,create_date,spu_state,fix_contact,email) "
			+ "VALUES (?,?,?,?,?,?,?,?)";

	// Define Supplier Update SQL
	private static final String UPD_SUPP_SQL = "UPDATE supplier_schema.supplier SET supp_name=:supp_name,address=:address,contact_number=:contact_number,fix_contact=:fix_contact,email=:email "
			+ "WHERE supp_id=:supp_id";

	// Define Supplier Delete SQL
	private static final String DEL_SUPP_SQL = "UPDATE supplier_schema.supplier SET spu_state=:spu_state WHERE supp_id=:supp_id";

	// Define Supplier Select SQL
	private static final String SEL_ALL_ACTIVE_SUPP = "SELECT supp_id,supp_name,address,contact_number,create_by,create_date,spu_state,fix_contact,email "
			+ "FROM supplier_schema.supplier WHERE spu_state=? ORDER BY supp_id ASC";

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
		LOGGER.info("<----------- Execute Create Batch Of Supppliyer In Repository batch Size{} ---------->",
				supplier.size());
		return this.jdbcTemplate.batchUpdate(CRE_BAT_SUPP_SQL, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, supplier.get(i).getSupplierName());
				ps.setString(2, supplier.get(i).getAddress());
				ps.setString(3, supplier.get(i).getContactNumber());
				ps.setString(4, supplier.get(i).getCreateBy());
				ps.setDate(5, new java.sql.Date(supplier.get(i).getCreateDate().getTime()));
				ps.setShort(6, supplier.get(1).getSuppState());
				ps.setString(7, supplier.get(i).getFixedNumber());
				ps.setString(8, supplier.get(i).getEmail());

			}

			@Override
			public int getBatchSize() {
				return supplier.size();
			}
		});

	}

	@Override
	public int updateSuppliyer(Supplier supplier) throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<----- Execute Update Suppliyer Name: {} In Repository ----> ", supplier.getSupplierName());
		int updState = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();

		paraMap.put("supp_name", supplier.getSupplierName());
		paraMap.put("address", supplier.getAddress());
		paraMap.put("contact_number", supplier.getContactNumber());
		paraMap.put("fix_contact", supplier.getFixedNumber());
		paraMap.put("email", supplier.getEmail());
		paraMap.put("supp_id", supplier.getSupplierId());

		updState = this.namedParameterJdbcTemplate.update(UPD_SUPP_SQL, paraMap);

		return updState;
	}

	@Override
	public int deleteSuppliyer(Supplier supplier) throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<----- Execute Delete Suppliyer Name: {} In Repository ----> ", supplier.getSupplierName());
		int delState = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();

		paraMap.put("supp_id", supplier.getSupplierId());
		paraMap.put("spu_state", supplier.getSuppState());

		delState = this.namedParameterJdbcTemplate.update(DEL_SUPP_SQL, paraMap);

		return delState;
	}

	@Override
	public Supplier getSuppliyerById(Integer suppId)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Supplier> getAllActiveSuppliyer(final short stateNumber)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<----- Execute Get All Active Suppliyers in Repositiry ---->");
		return this.jdbcTemplate.query(SEL_ALL_ACTIVE_SUPP, new SuppResExecutor(), stateNumber);
	}

	@Override
	public List<Supplier> getAllSuppliyer()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
