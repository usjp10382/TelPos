package com.teleios.pos.convertor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.teleios.pos.model.Category;

@FacesConverter("categoryConvertor")
public class CategoryConvertor implements Converter<Category> {

	@Override
	public Category getAsObject(FacesContext context, UIComponent component, String value) {
		Category selcCategory = null;
		if (value == null) {
			return null;
		} else {
			String[] str = value.split("-");
			selcCategory = new Category();
			int categoryId = Integer.parseInt(str[0]);
			selcCategory.setCategoryId(categoryId);
			selcCategory.setCategoryName(str[1]);
			return selcCategory;

		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Category value) {
		if (value != null) {
			StringBuilder categBuilder = new StringBuilder();
			categBuilder.append(value.getCategoryId()).append("-").append(value.getCategoryName());
			return categBuilder.toString();
		}
		return null;
	}

}
