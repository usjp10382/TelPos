package com.teleios.pos.convertor;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.teleios.pos.model.Product;

@FacesConverter(value = "prdConvertor")
public class ProductConvertor implements Converter<Product> {

	@Override
	public Product getAsObject(FacesContext context, UIComponent component, String value) {
		if (context == null)
			throw new NullPointerException("context");

		if (component == null)
			throw new NullPointerException("component");

		Product product = null;
		try {

			String[] strArr = value.split("_");
			product = new Product();
			product.setPrdId(Integer.parseInt(strArr[0]));
			product.setPrdCode(strArr[1]);
			product.setPrdName(strArr[2]);
			return product;
		} catch (NumberFormatException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown Product",
					"This is not a Product number/code/name!");
			throw new ConverterException(message);
		} catch (NullPointerException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown Suppliyer",
					"The Product is unknown!");
			throw new ConverterException(message);// TODO: handle exception
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Product value) {
		if (context == null)
			throw new NullPointerException("context");

		if (component == null)
			throw new NullPointerException("component");

		if (value != null) {
			StringBuilder suppBuilder = new StringBuilder();
			suppBuilder.append(value.getPrdId()).append("_").append(value.getPrdCode()).append("_")
					.append(value.getPrdName());
			return suppBuilder.toString();
		}
		return null;
	}

}
