package com.teleios.pos.dao.utill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.teleios.pos.model.Brand;
import com.teleios.pos.model.Category;
import com.teleios.pos.model.Product;
import com.teleios.pos.model.Uom;

public class ProductResExecutor implements ResultSetExtractor<List<Product>> {

	@Override
	public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Product> allActiveProducts = new ArrayList<Product>();

		while (rs.next()) {
			Product product = new Product();
			product.setPrdId(rs.getInt("prd_id"));
			product.setPrdCode(rs.getString("code"));
			product.setPrdName(rs.getString("prd_name"));
			product.setCreateBy(rs.getString("create_by"));
			product.setCreateDate(rs.getDate("create_date"));
			product.setState(rs.getShort("prd_state"));

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

			allActiveProducts.add(product);

		}
		return allActiveProducts;
	}

}
