package com.teleios.pos.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.teleios.pos.dao.impl.ExpenditureDaoImpl;
import com.teleios.pos.model.Expenditure;

@Service
public class ExpenditureService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpenditureService.class);

	@Autowired
	private ExpenditureDaoImpl expenditureDaoImpl;

	public int createNewExpendiure(Expenditure expenditure) throws Exception {
		return this.expenditureDaoImpl.createNewExpenditure(expenditure);
	}
	
	public int updateExpenditure(Expenditure expenditure) throws Exception { 
		return this.expenditureDaoImpl.updateExpenditure(expenditure);		
	}
	
	public int deleteExpenditure(Expenditure expenditure) throws Exception {
		return this.expenditureDaoImpl.deleteExpenditure(expenditure);	
	}

	public int getNextExpNumber() {
		int nextExpNumber = 1;
		try {
			nextExpNumber = this.expenditureDaoImpl.getNextExpNumber();
			nextExpNumber = nextExpNumber + 1;
		} catch (EmptyResultDataAccessException e) {
			return nextExpNumber;
		}
		return nextExpNumber;
	}

	public List<Expenditure> getAllExpenditures() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Exp Service--->");
		return this.expenditureDaoImpl.getAllExpenditure();
	}
}
