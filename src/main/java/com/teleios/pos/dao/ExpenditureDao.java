package com.teleios.pos.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.Expenditure;

public interface ExpenditureDao {

	public int createNewExpenditure(Expenditure expenditure) throws Exception;

	public int updateExpenditure(Expenditure expenditure) throws Exception;

	public int deleteExpenditure(Expenditure expenditure) throws Exception; 
	
	public int getNextExpNumber()throws EmptyResultDataAccessException,DataAccessException;

	public Expenditure getExpByNumber(int expNumber)throws EmptyResultDataAccessException,DataAccessException;

	public List<Expenditure> getAllExpenditure()throws EmptyResultDataAccessException,DataAccessException;
}
