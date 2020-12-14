package com.teleios.pos.controls;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Named
@SessionScoped
public class LocallController implements Serializable {
	/**
	 * 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LocallController.class);
	private static final long serialVersionUID = -9038665038301244991L;
	private Locale local = new Locale("en", "US");
	private String username;

	@PostConstruct
	public void init() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		setUsername(authentication.getName());

		try {
			Cookie cookie = getCookie("SelLang");
			if (cookie != null) {
				String selLang = cookie.getValue();
				if (selLang != null) {
					String[] loc = selLang.split("-");
					this.local = new Locale(loc[0], loc[1]);
					FacesContext.getCurrentInstance().getViewRoot().setLocale(this.local);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Get Cookie Erro-->", e);
		}
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
		try {
			setCookie("SelLang", getLocal().getLanguage().concat("-").concat(getLocal().getCountry()), 1000);
		} catch (Exception e) {
			LOGGER.error("Cookies Set Error--> ", e);
		}
	}

	public void setCookie(String name, String value, int expiry) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		Cookie cookie = null;

		Cookie[] userCookies = request.getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(name)) {
					cookie = userCookies[i];
					break;
				}
			}
		}

		if (cookie != null) {
			cookie.setValue(value);
		} else {
			cookie = new Cookie(name, value);
			cookie.setPath(request.getContextPath());
		}

		cookie.setMaxAge(expiry);

		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.addCookie(cookie);
	}

	public Cookie getCookie(String name) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		Cookie cookie = null;

		Cookie[] userCookies = request.getCookies();
		if (userCookies != null && userCookies.length > 0) {
			for (int i = 0; i < userCookies.length; i++) {
				if (userCookies[i].getName().equals(name)) {
					cookie = userCookies[i];
					return cookie;
				}
			}
		}
		return null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
