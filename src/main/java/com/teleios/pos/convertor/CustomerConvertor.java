package com.teleios.pos.convertor;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.teleios.pos.model.Customer;

@FacesConverter(value = "cusConvertor")
public class CustomerConvertor implements Converter<Customer> {

	@Override
	public Customer getAsObject(FacesContext context, UIComponent component, String value) {
		if (context == null)
			throw new NullPointerException("context");

		if (component == null)
			throw new NullPointerException("component");

		Customer customer = null;
		try {

			String[] strArr = value.split("_");
			customer = new Customer();
			customer.setCustomerId(Integer.parseInt(strArr[0]));
			customer.setFirstName(strArr[1]);
			customer.setLastName(strArr[2]);
			customer.setMobileNumber(strArr[3]);
			customer.setAddress(strArr[4]);
			return customer;
		} catch (NumberFormatException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown Customer",
					"This is not a Customer Number!");
			throw new ConverterException(message);
		} catch (NullPointerException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown Custoemr",
					"The Customer is unknown!");
			throw new ConverterException(message);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Customer value) {
		if (context == null)
			throw new NullPointerException("context");

		if (component == null)
			throw new NullPointerException("component");

		if (value != null) {
			StringBuilder cusBuilder = new StringBuilder();
			cusBuilder.append(value.getCustomerId()).append("_").append(value.getFirstName()).append("_")
					.append(value.getLastName()).append("_").append(value.getMobileNumber()).append("_")
					.append(value.getAddress());
			return cusBuilder.toString();
		}

		return null;
	}

}
