package com.teleios.pos.dao.utill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.teleios.pos.model.Customer;

public class CustomerResExecutor implements ResultSetExtractor<List<Customer>> {

	@Override
	public List<Customer> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Customer> customers = new ArrayList<Customer>();

		while (rs.next()) {
			Customer customer = new Customer(rs.getInt("customer_id"), rs.getString("first_name"),
					rs.getString("last_name"), rs.getString("address"), rs.getString("mobile_number"),
					rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("cus_state"));

			customers.add(customer);

		}

		return customers;
	}

}
