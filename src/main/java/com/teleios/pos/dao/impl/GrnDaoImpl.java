package com.teleios.pos.dao.impl;

import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.GrnDao;

@Repository
public class GrnDaoImpl implements GrnDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(GrnDaoImpl.class);

	// Define Grn Details Select SQL
	private static final String GET_NEXT_GRNNO_SQL = "SELECT MAX(grn_hdr_num) FROM inv_schema.grn_hdr";
	private static final String GET_NEXT_BATCHNO_SQL = "SELECT MAX(batch_number) FROM inv_schema.grn_hdr";

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public GrnDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public Integer getNextBatchNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<-- Fetching Next Grn Number In Repository ---->");
		return this.jdbcTemplate.queryForObject(GET_NEXT_GRNNO_SQL, Integer.class);
	}

	@Override
	public Integer getNextGrnNumber()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<-- Fetching Next Batch Number In Repository ---->");
		return this.jdbcTemplate.queryForObject(GET_NEXT_BATCHNO_SQL, Integer.class);
	}

}
