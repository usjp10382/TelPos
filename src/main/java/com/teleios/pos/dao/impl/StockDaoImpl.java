package com.teleios.pos.dao.impl;

import java.net.SocketTimeoutException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.StockDao;
import com.teleios.pos.dao.utill.StockPOSResExecutor;
import com.teleios.pos.model.GrnDet;
import com.teleios.pos.model.Stock;

@Repository
public class StockDaoImpl implements StockDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockDaoImpl.class);

	// Define Stock Create SQL
	private static final String CREATE_STOCK_ITEMS_SQL = "INSERT INTO inv_schema.stock (batch_no,grn_no,prd_id,purchase_price,selling_price,add_discout,exp_date,"
			+ "qty,line_val,state) VALUES (?,?,?,?,?,?,?,?,?,?)";

	// Define Fetch SQL For Stock
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
			+ "WHERE stock.qty > 0.00 ORDER BY stock.stock_id ASC";

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
	public List<Stock> getStockForPOS()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<--------- Execute Get Stock Items For POS In Stock Repository ---------->");
		return this.jdbcTemplate.query(SQL_STOCK_FOR_POS, new StockPOSResExecutor());
	}
}
