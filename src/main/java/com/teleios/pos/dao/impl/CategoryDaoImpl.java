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

import com.teleios.pos.dao.CategoryDao;
import com.teleios.pos.model.Category;

@Repository
public class CategoryDaoImpl implements CategoryDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDaoImpl.class);

	// Define Sql Query
	static final String CREATE_CATEG_SQL = "INSERT INTO inv_schema.category(categ_name,create_by,create_date,state) VALUES(:categ_name,:create_by,:create_date,:state)";
	static final String UPDATE_CATEG_SQL = "UPDATE inv_schema.category SET categ_name=:categ_name,create_by=:create_by,create_date=:create_date,state=:state "
			+ "WHERE categ_id=:categ_id";
	static final String DELETE_CATEG_SQL = "UPDATE inv_schema.category SET state=:state WHERE categ_id=:categ_id";
	static final String CATEG_BY_NUMBER_SQL = "SELECT categ_id,categ_name,create_by,create_date,state FROM inv_schema.category WHERE categ_id=?";
	static final String ACTIVE_CATEG_SQL = "SELECT categ_id,categ_name,create_by,create_date,state FROM inv_schema.category WHERE state=?";
	static final String ALL_CATEG_SQL = "SELECT categ_id,categ_name,create_by,create_date,state FROM inv_schema.category";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public int createNewCategory(Category category) throws DuplicateKeyException {
		LOGGER.info("Execute Create New Category Repositort-----------> ");
		int createstate = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("categ_name", category.getCategoryName());
		paraMap.put("create_by", category.getCreateBy());
		paraMap.put("create_date", category.getCreateDate());
		paraMap.put("state", (short) 1);
		createstate = this.namedParameterJdbcTemplate.update(CREATE_CATEG_SQL, paraMap);
		return createstate;
	}

	@Override
	public int updateCategory(Category category) throws DuplicateKeyException {
		LOGGER.info("Execute Update Category Repositort-----------> ");
		int updateState = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("categ_name", category.getCategoryName());
		paraMap.put("create_by", category.getCreateBy());
		paraMap.put("create_date", category.getCreateDate());
		paraMap.put("state", category.getState());
		paraMap.put("categ_id", category.getCategoryId());
		updateState = this.namedParameterJdbcTemplate.update(UPDATE_CATEG_SQL, paraMap);
		return updateState;
	}

	@Override
	public int deleteCategory(Category category) {
		LOGGER.info("Execute Delete Category Repositort-----------> ");
		int deleteState = 0;
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("state", (short) 0);
		paraMap.put("categ_id", category.getCategoryId());
		deleteState = this.namedParameterJdbcTemplate.update(DELETE_CATEG_SQL, paraMap);
		return deleteState;
	}

	@Override
	public Category getCategoryByNumber(Category category) throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Category By Number Repositort-----------> ");
		Category selectedCategory = null;
		selectedCategory = this.jdbcTemplate.queryForObject(CATEG_BY_NUMBER_SQL,
				new Object[] { category.getCategoryId() }, new RowMapper<Category>() {

					@Override
					public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
						Category category = new Category(rs.getInt("categ_id"), rs.getString("categ_name"),
								rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("state"));
						return category;
					}
				});
		return selectedCategory;
	}

	@Override
	public List<Category> getActiveCategories() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Active Categoryies Repositort-----------> ");
		List<Category> activeCategoryies = null;
		activeCategoryies = this.jdbcTemplate.query(ACTIVE_CATEG_SQL, new Object[] { (short) 1 },
				new RowMapper<Category>() {

					@Override
					public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
						Category category = new Category(rs.getInt("categ_id"), rs.getString("categ_name"),
								rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("state"));
						return category;
					}
				});
		return activeCategoryies;
	}

	@Override
	public List<Category> getAllCategories() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Active Categoryies Repositort-----------> ");
		List<Category> activeCategoryies = null;
		activeCategoryies = this.jdbcTemplate.query(ALL_CATEG_SQL, new RowMapper<Category>() {

			@Override
			public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
				Category category = new Category(rs.getInt("categ_id"), rs.getString("categ_name"),
						rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("state"));
				return category;
			}
		});
		return activeCategoryies;
	}

}
