package com.teleios.pos.convertor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.teleios.pos.model.Uom;

@FacesConverter("uomConvertor")
public class UomConvertor implements Converter<Uom> {
	

	@Override
	public Uom getAsObject(FacesContext context, UIComponent component, String value) {
		Uom selectedUom = null;
		if (value == null) {
			return null;
		} else {
			try {
				String[] strArr = value.split("_");
				selectedUom = new Uom();
				selectedUom.setUomId(Integer.parseInt(strArr[0]));
				selectedUom.setUomName(strArr[1]);
				selectedUom.setUomChar(strArr[2]);
				return selectedUom;
			} catch (NumberFormatException nfe) {
				return null;
			}
		}

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Uom value) {
		if (value != null) {
			StringBuilder uomBuilder = new StringBuilder();
			uomBuilder.append(value.getUomId()).append("_").append(value.getUomChar()).append("_")
					.append(value.getUomName());

			return uomBuilder.toString();
		}
		return null;
	}

}
