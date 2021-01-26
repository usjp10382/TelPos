package com.teleios.pos.dao.impl;

import java.net.SocketTimeoutException;
import java.sql.PreparedStatement;
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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.ProductDao;
import com.teleios.pos.dao.utill.ProductResExecutor;
import com.teleios.pos.model.Brand;
import com.teleios.pos.model.Category;
import com.teleios.pos.model.Product;
import com.teleios.pos.model.Uom;

@Repository
public class ProductDaoImpl implements ProductDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

	// Define Product Create SQL
	private static final String CREATE_PRD_SQL = "INSERT INTO inv_schema.product(code,prd_name,brand,category,uom,create_by,create_date,prd_state,min_qty_lev,rack_no)"
			+ " VALUES(:code,:prd_name,:brand,:category,:uom,:create_by,:create_date,:prd_state,:min_qty_lev,:rack_no)";
	static final String CREATE_BATCH_PRODUCTS_SQL = "INSERT INTO inv_schema.product(code,prd_name,brand,category,uom,create_by,create_date,prd_state,min_qty_lev,rack_no) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?)";

	// Define Product Update SQL
	private static final String UPD_PRD_SQL = "UPDATE inv_schema.product SET code=:code,prd_name=:prd_name,brand=:brand,category=:category,uom=:uom,"
			+ "create_by=:create_by,create_date=:create_date,prd_state=:prd_state,min_qty_lev=:min_qty_lev,rack_no=:rack_no WHERE prd_id=:prd_id";

	// Define Product Delete SQL
	private static final String DEL_PRD_SQL = "DELETE FROM inv_schema.product WHERE prd_id=?";

	// Define Product Search SQL
	private static final String SELECT_PRD_BYNUM_SQL = "SELECT p.prd_id,p.code,p.prd_name,p.create_by,p.create_date,p.prd_state,p.min_qty_lev,p.rack_no,"
			+ "b.brand_id,b.brand_name,c.categ_id,c.categ_name,u.uom_id,u.uom_name,u.char_prifix "
			+ "FROM inv_schema.product p INNER JOIN inv_schema.brand b ON P.brand=b.brand_id "
			+ "INNER JOIN inv_schema.category c ON p.category=c.categ_id "
			+ "INNER JOIN inv_schema.uom u ON p.uom=u.uom_id " + "WHERE p.prd_id=?";

	private static final String ALL_ACTIVE_PRD_SQL = "SELECT p.prd_id,p.code,p.prd_name,p.create_by,p.create_date,p.prd_state,p.min_qty_lev,p.rack_no,"
			+ "b.brand_id,b.brand_name,c.categ_id,c.categ_name,u.uom_id,u.uom_name,u.char_prifix "
			+ "FROM inv_schema.product p INNER JOIN inv_schema.brand b ON P.brand=b.brand_id "
			+ "INNER JOIN inv_schema.category c ON p.category=c.categ_id "
			+ "INNER JOIN inv_schema.uom u ON p.uom=u.uom_id " + "WHERE p.prd_state=? ORDER BY p.prd_id ASC";

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public ProductDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

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
	public int updateProduct(Product product) throws SocketTimeoutException, DuplicateKeyException, Exception {
		LOGGER.info("<----- Execute Update Product Name:{} Execute In Repositiry --->", product.getPrdName());
		int updState = 0;
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
		paraMap.put("prd_id", product.getPrdId());

		updState = this.namedParameterJdbcTemplate.update(UPD_PRD_SQL, paraMap);

		return updState;
	}

	@Override
	public int deleteProduct(Product product) throws SocketTimeoutException, Exception {
		LOGGER.info("<---- Execute Delete Product: {} in Repository ---->",
				product.getPrdCode() + " " + product.getPrdName());

		return this.jdbcTemplate.update(DEL_PRD_SQL, product.getPrdId());
	}

	@Override
	public Product getProductByNumber(Integer prdNumber)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<----- Execute Get Product By Number Product Number:{} In Product Repository---->", prdNumber);
		return this.jdbcTemplate.queryForObject(SELECT_PRD_BYNUM_SQL, new Object[] { prdNumber },
				new RowMapper<Product>() {

					@Override
					public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
						Product product = new Product();
						product.setPrdId(rs.getInt("prd_id"));
						product.setPrdCode(rs.getString("code"));
						product.setPrdName(rs.getString("prd_name"));
						product.setCreateBy(rs.getString("create_by"));
						product.setCreateDate(rs.getDate("create_date"));
						product.setState(rs.getShort("prd_state"));
						product.setRackDet(rs.getString("rack_no"));
						product.setMinQtyLevel(rs.getDouble("min_qty_lev"));

						Brand brand = new Brand();
						brand.setBrandId(rs.getInt("brand_id"));
						brand.setBrandName(rs.getString("brand_name"));

						product.setBrand(brand);

						Category category = new Category();
						category.setCategoryId(rs.getInt("categ_id"));
						category.setCategoryName(rs.getString("categ_name"));

						product.setCategory(category);

						Uom uom = new Uom();
						uom.setUomId(rs.getInt("uom_id"));
						uom.setUomName(rs.getString("uom_name"));
						uom.setUomChar(rs.getString("char_prifix"));

						product.setUom(uom);
						return product;
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
