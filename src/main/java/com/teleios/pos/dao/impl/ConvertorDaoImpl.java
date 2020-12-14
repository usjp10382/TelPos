package com.teleios.pos.dao.impl;

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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.ConvertorDao;
import com.teleios.pos.model.Convertor;

@Repository
public class ConvertorDaoImpl implements ConvertorDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConvertorDaoImpl.class);

	// Define Sql Query
	static final String CREATE_CONVERTOR_SQL = "INSERT INTO settings.convertor(base_uom_id,rat_uom_id,val,create_by,create_date,con_state) "
			+ "VALUES(:base_uom_id,:rat_uom_id,:val,:create_by,:create_date,:con_state)";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public int createNewConvertor(Convertor convertor) throws DuplicateKeyException, Exception {
		LOGGER.info("Execute Create New Convertor Repositort-----------> ");
		int createstate = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("base_uom_id", convertor.getBaseUom().getUomId());
		paraMap.put("rat_uom_id", convertor.getRatUom().getUomId());
		paraMap.put("val", convertor.getValue());
		paraMap.put("create_by", convertor.getCreateby());
		paraMap.put("create_date", convertor.getCreateDate());
		paraMap.put("con_state", (short) 1);
		createstate = this.namedParameterJdbcTemplate.update(CREATE_CONVERTOR_SQL, paraMap);
		return createstate;
	}

	@Override
	public int[][] createNewConvertor(List<Convertor> convertors) throws DuplicateKeyException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateConvertor(Convertor convertor) throws DuplicateKeyException, Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteConvertor(Convertor convertor) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Convertor getConvertorByNumber(Convertor convertor)
			throws EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Convertor> getActiveConvertors() throws EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Convertor> getAllConvertors() throws EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
