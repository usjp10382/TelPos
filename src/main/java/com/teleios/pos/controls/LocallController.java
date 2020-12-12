package com.teleios.pos.controls;

import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class LocallController {
	private Locale local = new Locale("en", "US");

	public Locale getLocal() {
		return local;
	}

	public void setLocal(Locale local) {
		this.local = local;
	}

	public void change(String lang) {
		String[] loc = lang.split("-");
		this.local = new Locale(loc[0], loc[1]);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(this.local);
	}
}
