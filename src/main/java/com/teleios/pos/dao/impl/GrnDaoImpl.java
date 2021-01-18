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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.teleios.pos.dao.GrnDao;
import com.teleios.pos.dao.utill.PayTypeResExecutor;
import com.teleios.pos.model.GrnDet;
import com.teleios.pos.model.GrnHdr;
import com.teleios.pos.model.PaymentType;

@Repository
public class GrnDaoImpl implements GrnDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(GrnDaoImpl.class);

	// Define Grn Insert SQL
	private static final String CREATE_GRN_HDR_SQL = "INSERT INTO inv_schema.grn_hdr (grn_hdr_num,batch_number,grn_date,total,fk_supp_id,create_by,fk_pur_type,create_date,grn_state,"
			+ "remark,cheq_det_id,is_tot_val_disc,grn_val_discount,disc_precentage,payble_amount,paid_amount,balance,item_count) VALUES "
			+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static final String CREATE_GRN_DETS_SQL = "INSERT INTO inv_schema.grn_det (unit_price,discount,purchase_price,selling_price,fk_prd_no,fk_grn_hdr_num,is_discount,qty,"
			+ "sub_total,expire_date,state) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	// Define Grn Details Select SQL
	private static final String GET_PAY_TYPES_SQL = "SELECT pay_state_id,description,create_by,create_date,pay_st_state FROM settings.pay_state WHERE pay_state_id<? ORDER BY pay_state_id ASC";
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
	public int createNewGrnHeder(GrnHdr grnHdr) throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("Create New Grn Header Executing... Grn Header No:{}---->", grnHdr.getGrnNumber());
		int saveState = 0;

		saveState = this.jdbcTemplate.update(CREATE_GRN_HDR_SQL, grnHdr.getGrnNumber(), grnHdr.getBatchNumber(),
				grnHdr.getGrnDate(), grnHdr.getTotalValue(), grnHdr.getSupplier().getSupplierId(), grnHdr.getCreateBy(),
				grnHdr.getPaymentType().getPayTypeId(), grnHdr.getCreateDate(), grnHdr.getGrnState(),
				grnHdr.getRemark(), Integer.valueOf(0), grnHdr.isTotValDiscount(),
				grnHdr.getTotalValue().subtract(grnHdr.getPaybleAmount()), grnHdr.getGrnValDiscount(),
				grnHdr.getPaybleAmount(), grnHdr.getPaidAmount(), grnHdr.getBalance(), grnHdr.getItemCount());

		LOGGER.info("New Grn Header Successfuly Created.. ");
		return saveState;
	}

	@Override
	public int[] createNewGrnDetails(List<GrnDet> grnDets)
			throws SocketTimeoutException, DataAccessException, Exception {
		LOGGER.info("Create New Grn Details Executing.....");
		int[] saveStates = null;

		saveStates = this.jdbcTemplate.batchUpdate(CREATE_GRN_DETS_SQL, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {

				ps.setBigDecimal(1, grnDets.get(i).getUnitPrice());
				ps.setBigDecimal(2, grnDets.get(i).getDiscount());
				ps.setBigDecimal(3, grnDets.get(i).getNetPrice());
				ps.setBigDecimal(4, grnDets.get(i).getSellingPrice());
				ps.setInt(5, grnDets.get(i).getProduct().getPrdId());
				ps.setInt(6, grnDets.get(i).getGrnHdr().getGrnNumber());
				ps.setBoolean(7, grnDets.get(i).isIsDiscount());
				ps.setBigDecimal(8, grnDets.get(i).getQty());
				ps.setBigDecimal(9, grnDets.get(i).getSubTotal());

				if (grnDets.get(i).getExpireDate() != null)
					ps.setDate(10, new java.sql.Date(grnDets.get(i).getExpireDate().getTime()));
				else
					ps.setDate(10, null);
				ps.setShort(11, grnDets.get(i).getState());

			}

			@Override
			public int getBatchSize() {
				return grnDets.size();
			}
		});

		LOGGER.info("Successfuly Created Nunmber Of :{} Items.....", saveStates.length);
		return saveStates;
	}

	@Override
	public List<PaymentType> getPaymentTypes(short maxRes)
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<--- Execute Geting Payment Types For Max:{} Repository--->", maxRes);
		return this.jdbcTemplate.query(GET_PAY_TYPES_SQL, new PayTypeResExecutor(), maxRes);
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
