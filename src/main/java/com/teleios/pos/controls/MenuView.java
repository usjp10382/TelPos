package com.teleios.pos.controls;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("menuView")
@RequestScoped
public class MenuView {

	public String gotoExpDetPage() {
		return "/settings/expenditure-view.xhtml";
	}
}
