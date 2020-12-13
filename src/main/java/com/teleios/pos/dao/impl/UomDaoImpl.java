package com.teleios.pos.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.UomDao;
import com.teleios.pos.model.Uom;

@Repository
public class UomDaoImpl implements UomDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(UomDaoImpl.class);

	// Define Sql Query
	static final String CREATE_UOM_SQL = "INSERT INTO inv_schema.uom(uom_name,char_prifix,create_by,create_date,uom_state) VALUES(:uom_name,:char_prifix,:create_by,:create_date,:uom_state)";
	static final String UPDATE_UOM_SQL = "UPDATE inv_schema.uom SET uom_name=:uom_name,char_prifix=:char_prifix,create_by=:create_by,create_date=:create_date,uom_state=:uom_state "
			+ "WHERE uom_id=:uom_id";
	static final String DELETE_UOM_SQL = "UPDATE inv_schema.uom SET uom_state=:uom_state WHERE uom_id=:uom_id";
	static final String UOM_BY_NUMBER_SQL = "SELECT uom_id,uom_name,char_prifix,create_by,create_date,uom_state FROM inv_schema.uom WHERE uom_id=?";
	static final String ACTIVE_UOM_SQL = "SELECT uom_id,uom_name,char_prifix,create_by,create_date,uom_state FROM inv_schema.uom WHERE uom_state=?";
	static final String ALL_UOM_SQL = "SELECT uom_id,uom_name,char_prifix,create_by,create_date,uom_state FROM inv_schema.uom";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public int createNewUom(Uom uom) throws DuplicateKeyException {
		LOGGER.info("Execute Create New Uom Repositort-----------> ");
		int createstate = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("uom_name", uom.getUomName());
		paraMap.put("char_prifix", uom.getUomChar().toUpperCase());
		paraMap.put("create_by", uom.getCreateBy());
		paraMap.put("create_date", uom.getCreateDate());
		paraMap.put("uom_state", (short) 1);
		createstate = this.namedParameterJdbcTemplate.update(CREATE_UOM_SQL, paraMap);
		return createstate;
	}

	@Override
	public int updateUom(Uom uom) throws DuplicateKeyException {
		LOGGER.info("Execute Update Uom Repositort-----------> ");
		int updateState = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("uom_name", uom.getUomName());
		paraMap.put("char_prifix", uom.getUomChar());
		paraMap.put("create_by", uom.getCreateBy());
		paraMap.put("create_date", uom.getCreateDate());
		paraMap.put("uom_state", uom.getState());
		paraMap.put("uom_id", uom.getUomId());
		updateState = this.namedParameterJdbcTemplate.update(UPDATE_UOM_SQL, paraMap);
		return updateState;
	}

	@Override
	public int deleteUom(Uom uom) {
		LOGGER.info("Execute Delete Uom Repositort-----------> ");
		int deleteState = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("uom_state", (short) 0);
		paraMap.put("uom_id", uom.getUomId());
		deleteState = this.namedParameterJdbcTemplate.update(DELETE_UOM_SQL, paraMap);
		return deleteState;
	}

	@Override
	public Uom getUomByNumber(Uom uom) throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Uom By Number Repositort-----------> ");
		Uom selectedUom = null;
		selectedUom = this.jdbcTemplate.queryForObject(UOM_BY_NUMBER_SQL, new Object[] { uom.getUomId() },
				new RowMapper<Uom>() {

					@Override
					public Uom mapRow(ResultSet rs, int rowNum) throws SQLException {
						Uom selUom = new Uom(rs.getInt("uom_id"), rs.getString("uom_name"), rs.getString("char_prifix"),
								rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("uom_state"));
						return selUom;
					}
				});
		return selectedUom;
	}

	@Override
	public List<Uom> getActiveUoms() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Active Uom Repositort-----------> ");
		List<Uom> activeUoms = null;
		activeUoms = this.jdbcTemplate.query(ACTIVE_UOM_SQL, new Object[] { (short) 1 }, new RowMapper<Uom>() {

			@Override
			public Uom mapRow(ResultSet rs, int rowNum) throws SQLException {
				Uom uom = new Uom(rs.getInt("uom_id"), rs.getString("uom_name"), rs.getString("char_prifix"),
						rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("uom_state"));
				return uom;
			}
		});
		return activeUoms;
	}

	@Override
	public List<Uom> getAllUoms() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get All Uom Repositort-----------> ");
		List<Uom> allUoms = null;
		allUoms = this.jdbcTemplate.query(ALL_UOM_SQL, new RowMapper<Uom>() {

			@Override
			public Uom mapRow(ResultSet rs, int rowNum) throws SQLException {
				Uom uom = new Uom(rs.getInt("uom_id"), rs.getString("uom_name"), rs.getString("char_prifix"),
						rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("uom_state"));
				return uom;
			}
		});
		return allUoms;
	}

}
