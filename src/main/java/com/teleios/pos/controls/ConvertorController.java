/**
	 * @author Harith Ahangama
	 * @Date 2020.12.12
	 * @Discription Manage bean for control Converter
	 */
package com.teleios.pos.controls;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.teleios.pos.model.ConvertorService;
import com.teleios.pos.model.Uom;
import com.teleios.pos.service.UomService;

@Named("convertorController")
@ViewScoped
public class ConvertorController implements Serializable{
	
	private static final long serialVersionUID = -4889998676755311383L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ConvertorController.class);
	
	//Inject Service Classes
	@Autowired
	private UomService uomService;
	@Autowired
	private ConvertorService convertorService;
	
	//Define Uom Variables
	private List<Uom> uoms;
	
	private Uom selBaseUom;
	
	@PostConstruct
	public void init() {
		LOGGER.info("<--------- Execute Convertor Bean Init ----------->");
		this.uoms=this.uomService.getActiveUoms();
	}

	public List<Uom> getUoms() {
		return uoms;
	}

	public void setUoms(List<Uom> uoms) {
		this.uoms = uoms;
	}

	public Uom getSelBaseUom() {
		return selBaseUom;
	}

	public void setSelBaseUom(Uom selBaseUom) {
		this.selBaseUom = selBaseUom;
		LOGGER.info("Selected UOM Obj---> "+selBaseUom.getUomName()+" - "+selBaseUom.getUomId());
	}
	
	
}
