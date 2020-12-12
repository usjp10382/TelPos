package com.teleios.pos.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.teleios.pos.dao.impl.UomDaoImpl;
import com.teleios.pos.model.Uom;

@Service
public class UomService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UomService.class);
	
	@Autowired
	private UomDaoImpl uomDaoImpl;

	public int createNewUom(Uom uom) throws DuplicateKeyException {
		LOGGER.info("Execute Create New Uom Service-----------> ");
		return this.uomDaoImpl.createNewUom(uom);
	}

	public int updateUom(Uom uom) throws DuplicateKeyException {
		LOGGER.info("Execute Update Uom Service-----------> ");
		return this.uomDaoImpl.updateUom(uom);
	}

	public int deleteUom(Uom uom) {
		LOGGER.info("Execute Delete Uom Service-----------> ");
		return this.uomDaoImpl.deleteUom(uom);
	}

	public Uom getUomByNumber(Uom uom) throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Uom By Number Service-----------> ");
		return this.uomDaoImpl.getUomByNumber(uom);
	}

	public List<Uom> getActiveUoms() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Active Uom Service-----------> ");
		return this.uomDaoImpl.getActiveUoms();
	}

	public List<Uom> getAllUoms() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get All Uom Service-----------> ");
		return this.uomDaoImpl.getAllUoms();
	}

}
