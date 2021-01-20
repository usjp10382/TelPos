package com.teleios.pos.convertor;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.teleios.pos.model.Brand;
import com.teleios.pos.model.Product;
import com.teleios.pos.model.Stock;
import com.teleios.pos.model.Uom;

@FacesConverter(value = "itemConvertor")
public class StockItemConvertor implements Converter<Stock>, Serializable {
	private static final long serialVersionUID = -4792065566667515777L;

	@Override
	public Stock getAsObject(FacesContext context, UIComponent component, String value) {
		if (context == null)
			throw new NullPointerException("context");

		if (component == null)
			throw new NullPointerException("component");

		Stock stock = null;
		try {

			String[] strArr = value.split("_");
			stock = new Stock();

			stock.setStockId(Integer.parseInt(strArr[0]));
			stock.setBatchNumber(Integer.parseInt(strArr[1]));
			stock.setPurchasePrice(new BigDecimal(strArr[2]));
			stock.setSellingPrice(new BigDecimal(strArr[3]));
			if (strArr[4].contentEquals("null"))
				stock.setAddDiscount(BigDecimal.ZERO);
			else
				stock.setAddDiscount(new BigDecimal(strArr[4]));

			Product product = new Product();
			product.setPrdId(Integer.parseInt(strArr[5]));
			product.setPrdCode(strArr[6]);
			product.setPrdName(strArr[7]);

			Brand brand = new Brand();
			brand.setBrandName(strArr[8]);
			product.setBrand(brand);

			Uom uom = new Uom();
			uom.setUomChar(strArr[9]);
			product.setUom(uom);

			stock.setProduct(product);

			return stock;
		} catch (NumberFormatException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown Product",
					"This is not a Product Number!");
			throw new ConverterException(message);
		} catch (NullPointerException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown Product",
					"The Product is unknown!");
			throw new ConverterException(message);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Stock value) {
		if (context == null)
			throw new NullPointerException("context");

		if (component == null)
			throw new NullPointerException("component");

		if (value != null) {
			StringBuilder stockBuilder = new StringBuilder();
			stockBuilder.append(value.getStockId()).append("_").append(value.getBatchNumber()).append("_")
					.append(value.getPurchasePrice()).append("_").append(value.getSellingPrice()).append("_")
					.append(value.getAddDiscount()).append("_").append(value.getProduct().getPrdId()).append("_")
					.append(value.getProduct().getPrdCode()).append("_").append(value.getProduct().getPrdName())
					.append("_").append(value.getProduct().getBrand().getBrandName()).append("_")
					.append(value.getProduct().getUom().getUomChar());

			return stockBuilder.toString();
		}
		return null;
	}

}
