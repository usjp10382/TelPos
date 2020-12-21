package com.teleios.pos.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

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

import com.teleios.pos.dao.BrandDao;
import com.teleios.pos.model.Brand;

@Repository
public class BrandDaoImpl implements BrandDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandDaoImpl.class);

	// Define Sql Query
	static final String CREATE_BRAND_SQL = "INSERT INTO inv_schema.brand(brand_name,create_by,create_date,brnd_state) VALUES(:brand_name,:create_by,:create_date,:brnd_state)";
	static final String UPDATE_BRAND_SQL = "UPDATE inv_schema.brand SET brand_name=:brand_name,create_by=:create_by,create_date=:create_date,brnd_state=:brnd_state "
			+ "WHERE brand_id=:brand_id";
	static final String DELETE_BRAND_SQL = "UPDATE inv_schema.brand SET brnd_state=:brnd_state WHERE brand_id=:brand_id";
	static final String BRAND_BY_NUMBER_SQL = "SELECT brand_id,brand_name,create_by,create_date,brnd_state FROM inv_schema.brand WHERE brand_id=?";
	static final String ACTIVE_BRAND_SQL = "SELECT brand_id,brand_name,create_by,create_date,brnd_state FROM inv_schema.brand WHERE brnd_state=?";
	static final String ALL_BRAND_SQL = "SELECT brand_id,brand_name,create_by,create_date,brnd_state FROM inv_schema.brand";

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public int createNewBrand(Brand brand) throws DuplicateKeyException {
		LOGGER.info("Execute Create New Brand Repositort-----------> ");
		System.out.println("*******  Datasouce** "+dataSource);
		int createstate = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("brand_name", brand.getBrandName());
		paraMap.put("create_by", brand.getCreateBy());
		paraMap.put("create_date", brand.getCreateDate());
		paraMap.put("brnd_state", (short) 1);
		createstate = this.namedParameterJdbcTemplate.update(CREATE_BRAND_SQL, paraMap);
		return createstate;
	}

	@Override
	public int updateBrand(Brand brand) throws DuplicateKeyException {
		LOGGER.info("Execute Update Brand Repositort-----------> ");
		int updateState = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("brand_name", brand.getBrandName());
		paraMap.put("create_by", brand.getCreateBy());
		paraMap.put("create_date", brand.getCreateDate());
		paraMap.put("brnd_state", brand.getState());
		paraMap.put("brand_id", brand.getBrandId());
		updateState = this.namedParameterJdbcTemplate.update(UPDATE_BRAND_SQL, paraMap);
		return updateState;
	}

	@Override
	public int deleteBrand(Brand brand) {
		LOGGER.info("Execute Delete Brand Repositort-----------> ");
		int deleteState = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("brnd_state", (short) 0);
		paraMap.put("brand_id", brand.getBrandId());
		deleteState = this.namedParameterJdbcTemplate.update(DELETE_BRAND_SQL, paraMap);
		return deleteState;
	}

	@Override
	public Brand getBrandByNumber(Brand brand) throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Brand By Number Repositort-----------> ");
		Brand selectedBrand = null;
		selectedBrand = this.jdbcTemplate.queryForObject(BRAND_BY_NUMBER_SQL, new Object[] { brand.getBrandId() },
				new RowMapper<Brand>() {

					@Override
					public Brand mapRow(ResultSet rs, int rowNum) throws SQLException {
						Brand selBrand = new Brand(rs.getInt("brand_id"), rs.getString("brand_name"),
								rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("brnd_state"));
						return selBrand;
					}
				});
		return selectedBrand;
	}

	@Override
	public List<Brand> getActiveBrands() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Active Brands Repositort-----------> ");
		List<Brand> activeBrands = null;
		activeBrands = this.jdbcTemplate.query(ACTIVE_BRAND_SQL, new Object[] { (short) 1 }, new RowMapper<Brand>() {

			@Override
			public Brand mapRow(ResultSet rs, int rowNum) throws SQLException {
				Brand selBrand = new Brand(rs.getInt("brand_id"), rs.getString("brand_name"), rs.getString("create_by"),
						rs.getDate("create_date"), rs.getShort("brnd_state"));
				return selBrand;
			}
		});
		return activeBrands;
	}

	@Override
	public List<Brand> getAllBrands() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Active Brands Repositort-----------> ");
		List<Brand> allBrands = null;
		allBrands = this.jdbcTemplate.query(ALL_BRAND_SQL, new RowMapper<Brand>() {

			@Override
			public Brand mapRow(ResultSet rs, int rowNum) throws SQLException {
				Brand selBrand = new Brand(rs.getInt("brand_id"), rs.getString("brand_name"), rs.getString("create_by"),
						rs.getDate("create_date"), rs.getShort("brnd_state"));
				return selBrand;
			}
		});
		return allBrands;
	}

}
