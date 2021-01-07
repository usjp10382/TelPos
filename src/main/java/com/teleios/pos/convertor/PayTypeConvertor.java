package com.teleios.pos.convertor;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.teleios.pos.model.PaymentType;

@FacesConverter("payConvertor")
public class PayTypeConvertor implements Converter<PaymentType> {

	@Override
	public PaymentType getAsObject(FacesContext context, UIComponent component, String value) {
		if (context == null)
			throw new NullPointerException("context");

		if (component == null)
			throw new NullPointerException("component");

		PaymentType paymentType = null;
		try {

			String[] strArr = value.split("_");
			paymentType = new PaymentType();
			paymentType.setPayTypeId(Short.parseShort(strArr[0]));
			paymentType.setDesc(strArr[1]);
			return paymentType;
		} catch (NumberFormatException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown Suppliyer",
					"This is not a Suppliyer number!");
			throw new ConverterException(message);
		} catch (NullPointerException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown Suppliyer",
					"The Suppliyer is unknown!");
			throw new ConverterException(message);// TODO: handle exception
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, PaymentType value) {
		if (context == null)
			throw new NullPointerException("context");

		if (component == null)
			throw new NullPointerException("component");

		if (value != null) {
			StringBuilder suppBuilder = new StringBuilder();
			suppBuilder.append(value.getPayTypeId()).append("_").append(value.getDesc());
			return suppBuilder.toString();
		}
		return null;
	}

}
