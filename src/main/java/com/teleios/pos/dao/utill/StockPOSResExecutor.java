package com.teleios.pos.dao.utill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.teleios.pos.model.Brand;
import com.teleios.pos.model.Product;
import com.teleios.pos.model.Stock;
import com.teleios.pos.model.Uom;

public class StockPOSResExecutor implements ResultSetExtractor<List<Stock>> {

	@Override
	public List<Stock> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Stock> stocks = new ArrayList<Stock>();

		while (rs.next()) {

			Stock stock = new Stock();
			stock.setStockId(rs.getInt("stock_stock_id"));
			stock.setBatchNumber(rs.getInt("stock_batch_no"));
			stock.setPurchasePrice(rs.getBigDecimal("stock_purchase_price"));
			stock.setSellingPrice(rs.getBigDecimal("stock_selling_price"));
			stock.setAddDiscount(rs.getBigDecimal("stock_add_discout"));
			stock.setExpDate(rs.getDate("stock_exp_date"));
			stock.setQty(rs.getBigDecimal("stock_qty"));

			Product product = new Product();
			product.setPrdId(rs.getInt("product_prd_id"));
			product.setPrdCode(rs.getString("product_code"));
			product.setPrdName(rs.getString("product_prd_name"));

			Uom uom = new Uom();
			uom.setUomChar(rs.getString("uom_char_prifix"));
			product.setUom(uom);

			Brand brand = new Brand();
			brand.setBrandName(rs.getString("brand_brand_name"));
			product.setBrand(brand);

			stock.setProduct(product);

			stocks.add(stock);
		}

		return stocks;
	}

}
