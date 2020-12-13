package com.teleios.pos.controls;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Named
@SessionScoped
public class LocallController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9038665038301244991L;
	private Locale local = new Locale("en", "US");
	private String username;

	@PostConstruct
	public void init() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		setUsername(authentication.getName());
	}

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
