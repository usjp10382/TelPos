package com.teleios.pos.dao.utill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.teleios.pos.model.PaymentType;

public class PayTypeResExecutor implements ResultSetExtractor<List<PaymentType>> {

	@Override
	public List<PaymentType> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<PaymentType> payTypes = new ArrayList<PaymentType>();
		while (rs.next()) {
			PaymentType payType = new PaymentType(rs.getShort("pay_state_id"), rs.getString("description"),
					rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("pay_st_state"));

			payTypes.add(payType);

		}
		return payTypes;
	}

}
