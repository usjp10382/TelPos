package com.teleios.pos.convertor;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.teleios.pos.model.Supplier;

@FacesConverter("suppConvertor")
public class SuppConvertor implements Converter<Supplier> {

	@Override
	public Supplier getAsObject(FacesContext context, UIComponent component, String value) {
		if (context == null)
			throw new NullPointerException("context");

		if (component == null)
			throw new NullPointerException("component");

		Supplier selSupplier = null;
		try {

			String[] strArr = value.split("_");
			selSupplier = new Supplier();
			selSupplier.setSupplierId(Integer.parseInt(strArr[0]));
			selSupplier.setSupplierName(strArr[1]);
			return selSupplier;
		} catch (NumberFormatException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown Suppliyer",
					"This is not a Suppliyer number!");
			throw new ConverterException(message);
		} catch (NullPointerException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown Suppliyer",
					"The Suppliyer is unknown!");
			throw new ConverterException(message);// TODO: handle exception
		}

		// return selSupplier;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Supplier value) {
		if (context == null)
			throw new NullPointerException("context");

		if (component == null)
			throw new NullPointerException("component");

		if (value != null) {
			StringBuilder suppBuilder = new StringBuilder();
			suppBuilder.append(value.getSupplierId()).append("_").append(value.getSupplierName());
			return suppBuilder.toString();
		}
		return null;

	}

}
