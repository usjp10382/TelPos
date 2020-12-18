package com.teleios.pos.convertor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.teleios.pos.controls.ProductController;
import com.teleios.pos.model.Category;

@FacesConverter("categoryConvertor")
public class CategoryConvertor implements Converter<Category> {

	@Override
	public Category getAsObject(FacesContext context, UIComponent component, String value) {
		ProductController productController = context.getApplication().evaluateExpressionGet(context,
				"#{prdController}", ProductController.class);
		for (Category categ : productController.getAllActiveCategories()) {
			return categ;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Category value) {
		if (value != null) {
			Category category = value;

			return category.getCategoryName();
		}
		return null;
	}

}
