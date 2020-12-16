package com.teleios.pos.utill;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.teleios.pos.model.Uom;

public class UomRawMapper implements RowMapper<Uom> {

	@Override
	public Uom mapRow(ResultSet rs, int rowNum) throws SQLException {
		Uom uom = new Uom(rs.getInt("uom_id"), rs.getString("uom_name"), rs.getString("char_prifix"),
				rs.getString("create_by"), rs.getDate("create_date"), rs.getShort("uom_state"));
		return uom;

	}

}
