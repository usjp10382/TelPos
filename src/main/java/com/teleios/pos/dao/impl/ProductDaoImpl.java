package com.teleios.pos.dao.impl;

import java.net.SocketTimeoutException;
import java.sql.PreparedStatement;
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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.ProductDao;
import com.teleios.pos.dao.utill.ProductResExecutor;
import com.teleios.pos.model.Product;

@Repository
public class ProductDaoImpl implements ProductDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

	private static final String CREATE_PRD_SQL = "INSERT INTO inv_schema.product(code,prd_name,brand,category,uom,create_by,create_date,prd_state,min_qty_lev,rack_no)"
			+ " VALUES(:code,:prd_name,:brand,:category,:uom,:create_by,:create_date,:prd_state,:min_qty_lev,:rack_no)";
	static final String CREATE_BATCH_PRODUCTS_SQL = "INSERT INTO inv_schema.product(code,prd_name,brand,category,uom,create_by,create_date,prd_state,min_qty_lev,rack_no) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?)";

	private static final String ALL_ACTIVE_PRD_SQL = "SELECT p.prd_id,p.code,p.prd_name,p.create_by,p.create_date,p.prd_state,p.min_qty_lev,p.rack_no,"
			+ "b.brand_id,b.brand_name,c.categ_id,c.categ_name,u.uom_id,u.uom_name,u.char_prifix "
			+ "FROM inv_schema.product p INNER JOIN inv_schema.brand b ON P.brand=b.brand_id "
			+ "INNER JOIN inv_schema.category c ON p.category=c.categ_id "
			+ "INNER JOIN inv_schema.uom u ON p.uom=u.uom_id " + "WHERE p.prd_state=? ORDER BY p.prd_id ASC";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public int createNewProduct(Product product) throws SocketTimeoutException, DuplicateKeyException, Exception {
		LOGGER.info("<-------- Execute Create New Product Repository ----->");
		int saveState = 0;

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("code", product.getPrdCode().toLowerCase());
		paraMap.put("prd_name", product.getPrdName());
		paraMap.put("brand", product.getBrand().getBrandId());
		paraMap.put("category", product.getCategory().getCategoryId());
		paraMap.put("uom", product.getUom().getUomId());
		paraMap.put("create_by", product.getCreateBy());
		paraMap.put("create_date", product.getCreateDate());
		paraMap.put("prd_state", (short) 1);
		paraMap.put("min_qty_lev", product.getMinQtyLevel());
		paraMap.put("rack_no", product.getRackDet());

		saveState = this.namedParameterJdbcTemplate.update(CREATE_PRD_SQL, paraMap);

		return saveState;
	}

	@Override
	public int[] createNewProduct(List<Product> products)
			throws SocketTimeoutException, DuplicateKeyException, Exception {
		LOGGER.info("<----------- Execute Create Batch Of Product In Repository ---------->");

		return this.jdbcTemplate.batchUpdate(CREATE_BATCH_PRODUCTS_SQL, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, products.get(i).getPrdCode());
				ps.setString(2, products.get(i).getPrdName());
				ps.setInt(3, products.get(i).getBrand().getBrandId());
				ps.setInt(4, products.get(i).getCategory().getCategoryId());
				ps.setInt(5, products.get(i).getUom().getUomId());
				ps.setString(6, products.get(i).getCreateBy());
				ps.setDate(7, new java.sql.Date(products.get(i).getCreateDate().getTime()));
				ps.setShort(8, products.get(i).getState());
				ps.setDouble(9, products.get(i).getMinQtyLevel());
				ps.setString(10, products.get(i).getRackDet());

			}

			@Override
			public int getBatchSize() {
				return products.size();
			}
		});

	}

	@Override
	public List<Product> getAllActiveProducts()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<----------- Execute Get All Active Products Repository ------------->");
		List<Product> allActiveProducts = null;
		allActiveProducts = this.jdbcTemplate.query(ALL_ACTIVE_PRD_SQL, new ProductResExecutor(), (short) 1);
		return allActiveProducts;
	}

}
