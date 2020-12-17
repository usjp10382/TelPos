package com.teleios.pos.dao.utill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.teleios.pos.model.Expenditure;

public class ExpenditureResExecutor implements ResultSetExtractor<List<Expenditure>> {

	@Override
	public List<Expenditure> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Expenditure> expenditures = new ArrayList<Expenditure>();

		while (rs.next()) {
			Expenditure exp = new Expenditure(rs.getInt("exp_det_id"), rs.getString("description"),
					rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("exp_det_state"));
			expenditures.add(exp);

		}
		return expenditures;
	}

}
