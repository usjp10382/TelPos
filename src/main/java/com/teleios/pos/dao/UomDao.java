package com.teleios.pos.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.Uom;

public interface UomDao {
	int createNewUom(Uom uom) throws DuplicateKeyException;

	int updateUom(Uom uom) throws DuplicateKeyException;

	int deleteUom(Uom uom);

	Uom getUomByNumber(Uom uom) throws EmptyResultDataAccessException, DataAccessException;

	List<Uom> getActiveUoms() throws EmptyResultDataAccessException, DataAccessException;

	List<Uom> getAllUoms() throws EmptyResultDataAccessException, DataAccessException;
}
