package com.teleios.pos.dao.utill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.teleios.pos.model.Supplier;

public class SuppResExecutor implements ResultSetExtractor<List<Supplier>> {

	@Override
	public List<Supplier> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Supplier> suppliers = new ArrayList<Supplier>();
		while (rs.next()) {
			Supplier supplier = new Supplier(rs.getInt("supp_id"), rs.getString("supp_name"), rs.getString("address"),
					rs.getString("contact_number"), rs.getString("fix_contact"), rs.getString("email"),
					rs.getDate("create_date"), rs.getString("create_by"), rs.getShort("spu_state"));

			suppliers.add(supplier);
		}
		return suppliers;
	}

}
