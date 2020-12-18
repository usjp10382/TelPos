package com.teleios.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.teleios.pos.dao.impl.ConvertorDaoImpl;
import com.teleios.pos.model.Convertor;

@Service
public class ConvertorService {
	@Autowired
	private ConvertorDaoImpl convertoDaoImpl;

	public int createNewConvertor(Convertor convertor) throws DuplicateKeyException, Exception {
		return this.convertoDaoImpl.createNewConvertor(convertor);
	}

	public int[] createNewConvertor(List<Convertor> convertors) throws DuplicateKeyException, Exception {
		return this.convertoDaoImpl.createNewConvertor(convertors);
	}

	public int updateConvertor(Convertor convertor) throws DuplicateKeyException, Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	public int deleteConvertor(Convertor convertor) throws Exception {
		return this.convertoDaoImpl.deleteConvertor(convertor);
	}

	public Convertor getConvertorByNumber(Convertor convertor)
			throws EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Convertor> getActiveConvertors() throws EmptyResultDataAccessException, DataAccessException, Exception {
		return this.convertoDaoImpl.getActiveConvertors();
	}

	public List<Convertor> getAllConvertors() throws EmptyResultDataAccessException, DataAccessException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
