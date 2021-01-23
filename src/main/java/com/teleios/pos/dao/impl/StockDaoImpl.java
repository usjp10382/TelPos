package com.teleios.pos.dao.impl;

import java.net.SocketTimeoutException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.StockDao;
import com.teleios.pos.dao.utill.StockPOSResExecutor;
import com.teleios.pos.model.Brand;
import com.teleios.pos.model.Category;
import com.teleios.pos.model.GrnDet;
import com.teleios.pos.model.InvDet;
import com.teleios.pos.model.Product;
import com.teleios.pos.model.Stock;
import com.teleios.pos.model.Uom;

@Repository
public class StockDaoImpl implements StockDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockDaoImpl.class);

	// Define Stock Create SQL
	private static final String CREATE_STOCK_ITEMS_SQL = "INSERT INTO inv_schema.stock (batch_no,grn_no,prd_id,purchase_price,selling_price,add_discout,exp_date,"
			+ "qty,line_val,state) VALUES (?,?,?,?,?,?,?,?,?,?)";

	// Define Update Stock SQL
	private static final String UPD_STOCK_FOR_NEWINV = "UPDATE inv_schema.stock SET qty=qty-? WHERE stock_id=? AND batch_no=?";

	// Define Fetch SQL For Stock
	private static final String SQL_STOCK_BY_NUMBER = "SELECT\r\n"
			+ "     stock.stock_id, stock.batch_no, stock.grn_no, stock.prd_id, stock.purchase_price,\r\n"
			+ "     stock.selling_price, stock.add_discout, stock.exp_date, stock.qty, product.prd_id,\r\n"
			+ "     product.code,product.prd_name,product.brand,product.category,product.uom,\r\n"
			+ "     product.prd_state,product.min_qty_lev, product.rack_no, uom.uom_id, uom.uom_name,\r\n"
			+ "     uom.char_prifix, category.categ_id, category.categ_name, category.create_by,\r\n"
			+ "     category.create_date,  category.state, brand.brand_id,brand.brand_name\r\n" + "FROM\r\n"
			+ "     inv_schema.product product INNER JOIN inv_schema.stock stock ON product.prd_id = stock.prd_id\r\n"
			+ "     INNER JOIN inv_schema.uom uom ON product.uom = uom.uom_id\r\n"
			+ "     INNER JOIN inv_schema.category category ON product.category = category.categ_id\r\n"
			+ "     INNER JOIN inv_schema.brand brand ON product.brand = brand.brand_id\r\n" + "WHERE stock.stock_id=?";

	private static final String SQL_STOCK_FOR_POS = "SELECT\r\n" + "     stock.stock_id AS stock_stock_id,\r\n"
			+ "     stock.batch_no AS stock_batch_no,\r\n" + "     stock.prd_id AS stock_prd_id,\r\n"
			+ "     stock.purchase_price AS stock_purchase_price,\r\n"
			+ "     stock.selling_price AS stock_selling_price,\r\n"
			+ "     stock.add_discout AS stock_add_discout,\r\n" + "     stock.exp_date AS stock_exp_date,\r\n"
			+ "     stock.qty AS stock_qty,\r\n" + "     product.prd_id AS product_prd_id,\r\n"
			+ "     product.code AS product_code,\r\n" + "     product.prd_name AS product_prd_name,\r\n"
			+ "     uom.char_prifix AS uom_char_prifix,\r\n" + "     brand.brand_name AS brand_brand_name\r\n"
			+ "FROM\r\n"
			+ "     inv_schema.product product INNER JOIN inv_schema.stock stock ON product.prd_id = stock.prd_id\r\n"
			+ "     INNER JOIN inv_schema.uom uom ON product.uom = uom.uom_id\r\n"
			+ "     INNER JOIN inv_schema.brand brand ON product.brand = brand.brand_id\r\n"
			+ "WHERE stock.qty > 0.00 ORDER BY stock.batch_no ASC";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public StockDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int[] createNewStockItems(List<GrnDet> grnDets, Integer batchNumber)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("Executing.. Create New Stock Items Number Of: {} Items---->", grnDets.size());
		int[] saveStates = null;

		saveStates = this.jdbcTemplate.batchUpdate(CREATE_STOCK_ITEMS_SQL, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {

				ps.setInt(1, batchNumber);
				ps.setInt(2, grnDets.get(i).getGrnHdr().getGrnNumber());
				ps.setInt(3, grnDets.get(i).getProduct().getPrdId());
				ps.setBigDecimal(4, grnDets.get(i).getNetPrice());
				ps.setBigDecimal(5, grnDets.get(i).getSellingPrice());
				ps.setBigDecimal(6, grnDets.get(i).getDiscount());

				if (grnDets.get(i).getExpireDate() != null)
					ps.setDate(7, new java.sql.Date(grnDets.get(i).getExpireDate().getTime()));
				else
					ps.setDate(7, null);

				ps.setBigDecimal(8, grnDets.get(i).getQty());
				ps.setBigDecimal(9, grnDets.get(i).getSubTotal());
				ps.setShort(10, grnDets.get(i).getState());

			}

			@Override
			public int getBatchSize() {
				return grnDets.size();
			}
		});

		LOGGER.info("Successfuly CreateStock Items Number Of:{} Items---->", saveStates.length);
		return saveStates;
	}

	@Override
	public Stock getStockItemByNumber(Integer stockItem)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("Execute Get Stock By Number Sctock Item Number:{} Repository", stockItem);
		return this.jdbcTemplate.queryForObject(SQL_STOCK_BY_NUMBER, new Object[] { stockItem },
				new RowMapper<Stock>() {
					@Override
					public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
						Stock stock = new Stock();
						stock.setStockId(rs.getInt("stock_id"));
						stock.setBatchNumber(rs.getInt("batch_no"));
						stock.setGrnNumber(rs.getInt("grn_no"));
						stock.setPurchasePrice(rs.getBigDecimal("purchase_price"));
						stock.setSellingPrice(rs.getBigDecimal("selling_price"));
						stock.setAddDiscount(rs.getBigDecimal("add_discout"));
						stock.setExpDate(rs.getDate("exp_date"));
						stock.setQty(rs.getBigDecimal("qty"));

						Product product = new Product();
						product.setPrdId(rs.getInt("prd_id"));
						product.setPrdCode(rs.getString("code"));
						product.setPrdName(rs.getString("prd_name"));
						product.setState(rs.getShort("prd_state"));
						product.setMinQtyLevel(rs.getDouble("min_qty_lev"));
						product.setRackDet(rs.getString("rack_no"));

						Uom uom = new Uom();
						uom.setUomId(rs.getInt("uom_id"));
						uom.setUomName(rs.getString("uom_name"));
						uom.setUomChar(rs.getString("char_prifix"));

						Category category = new Category();
						category.setCategoryId(rs.getInt("categ_id"));
						category.setCategoryName(rs.getString("categ_name"));

						Brand brand = new Brand();
						brand.setBrandId(rs.getInt("brand_id"));
						brand.setBrandName(rs.getString("brand_name"));

						product.setUom(uom);
						product.setCategory(category);
						product.setBrand(brand);
						stock.setProduct(product);

						return stock;
					}
				});
	}

	@Override
	public int[] updateStockForNewInvoice(List<InvDet> invDets)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("<----------- Execute Update Stock For Pos In Stock Repository --------->");
		int[] updateStates = null;
		updateStates = this.jdbcTemplate.batchUpdate(UPD_STOCK_FOR_NEWINV, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setBigDecimal(1, invDets.get(i).getQty());
				ps.setInt(2, invDets.get(i).getStockItem().getStockId());
				ps.setInt(3, invDets.get(i).getStockItem().getBatchNumber());
			}

			@Override
			public int getBatchSize() {
				return invDets.size();
			}
		});
		LOGGER.info("<----------- Successfuly Update Number Of :{} Stock Items In Stock Repository --------->",
				updateStates.length);
		return updateStates;
	}

	@Override
	public List<Stock> getStockForPOS()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<--------- Execute Get Stock Items For POS In Stock Repository ---------->");
		return this.jdbcTemplate.query(SQL_STOCK_FOR_POS, new StockPOSResExecutor());
	}

}
