package com.teleios.pos.controls;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("userController")
@ViewScoped
public class UserController implements Serializable {
	private static final long serialVersionUID = 8315128850950559326L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	private boolean skip;

	@PostConstruct
	public void init() {
		LOGGER.info("<------------ Execute User Controler Inti ------------>");

	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String onFlowProcess(FlowEvent event) {
		if (skip) {
			skip = false; // reset in case user goes back
			return "confirm";
		} else {
			return event.getNewStep();
		}
	}
}
