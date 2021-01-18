package com.teleios.pos.dao.impl;

import java.net.SocketTimeoutException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.StockDao;
import com.teleios.pos.model.GrnDet;

@Repository
public class StockDaoImpl implements StockDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockDaoImpl.class);

	private static final String CREATE_STOCK_ITEMS_SQL = "INSERT INTO inv_schema.stock (batch_no,grn_no,prd_id,purchase_price,selling_price,add_discout,exp_date,"
			+ "qty,line_val,state) VALUES (?,?,?,?,?,?,?,?,?,?)";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public StockDaoImpl(JdbcTemplate jdbcTemplate) {
		super();
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
}
