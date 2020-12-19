package com.teleios.pos.convertor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.teleios.pos.model.Brand;

@FacesConverter("brandConvertor")
public class BrandConvertor implements Converter<Brand> {

	@Override
	public Brand getAsObject(FacesContext context, UIComponent component, String value) {
		Brand selectedBrand = null;
		if (value == null) {
			return null;
		} else {
			String[] strArr = value.split("-");
			selectedBrand = new Brand();
			selectedBrand.setBrandId(Integer.parseInt(strArr[0]));
			selectedBrand.setBrandName(strArr[1]);
			return selectedBrand;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Brand value) {
		if (value != null) {
			StringBuilder brandBuilder = new StringBuilder();
			brandBuilder.append(value.getBrandId()).append("-").append(value.getBrandName());

			return brandBuilder.toString();
		}
		return null;
	}

}
