package com.teleios.pos.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.ExpenditureDao;
import com.teleios.pos.model.Expenditure;

@Repository
public class ExpenditureDaoImpl implements ExpenditureDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpenditureDaoImpl.class);
	@Autowired
	private JdbcTemplate jdbctemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public int createNewExpenditure(Expenditure expenditure) throws Exception {
		String SQL = "INSERT INTO expen_schema.exp_det (description,create_by,create_date,exp_det_state) VALUES(:description,:create_by,:create_date,:exp_det_state)";
		int saveState = 0;

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("description", expenditure.getDec());
		paraMap.put("create_by", expenditure.getCreateBy());
		paraMap.put("create_date", new Date());
		paraMap.put("exp_det_state", new Short("1"));

		saveState = this.namedParameterJdbcTemplate.update(SQL, paraMap);

		return saveState;
	}

	@Override
	public int updateExpenditure(Expenditure expenditure) throws Exception {
		LOGGER.info("Call to update SQL..............");
		int updateState = 0;
		String SQL = "UPDATE expen_schema.exp_det SET description= :description, create_by= :create_by,create_date= :create_date WHERE exp_det_id=:exp_det_id";

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("description", expenditure.getDec());
		paraMap.put("create_by", "Teleios");
		paraMap.put("create_date", new Date());
		paraMap.put("exp_det_id", expenditure.getExpId());

		updateState = this.namedParameterJdbcTemplate.update(SQL, paraMap);

		return updateState;
	}

	@Override
	public int deleteExpenditure(Expenditure expenditure) throws Exception {
		LOGGER.info("Call to Delete SQL..............");
		int deleteState = 0;
		String SQL = "DELETE FROM expen_schema.exp_det WHERE exp_det_id=:exp_det_id";

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("exp_det_id", expenditure.getExpId());

		deleteState = this.namedParameterJdbcTemplate.update(SQL, paraMap);

		return deleteState;
	}

	@Override
	public int getNextExpNumber() throws EmptyResultDataAccessException, DataAccessException {
		String SQL = "SELECT MAX(exp_det_id) FROM expen_schema.exp_det";
		int nextExpId = 0;
		nextExpId = this.jdbctemplate.queryForObject(SQL, Integer.class);
		return nextExpId;

	}

	@Override
	public Expenditure getExpByNumber(int expNumber) throws EmptyResultDataAccessException, DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Expenditure> getAllExpenditure() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute ExpeDao Imple Get All Expss---------->");
		String SQL = "SELECT exp_det_id,description,create_by,create_date,exp_det_state FROM expen_schema.exp_det ORDER BY exp_det_id ASC ";
		List<Expenditure> expenditures = null;

		expenditures = this.jdbctemplate.query(SQL, new RowMapper<Expenditure>() {

			@Override
			public Expenditure mapRow(ResultSet rs, int rowNum) throws SQLException {
				Expenditure exp = new Expenditure(rs.getInt("exp_det_id"), rs.getString("description"),
						rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("exp_det_state"));
				return exp;
			}
		});

		return expenditures;
	}

}
