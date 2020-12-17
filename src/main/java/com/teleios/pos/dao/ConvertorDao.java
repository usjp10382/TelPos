package com.teleios.pos.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.Convertor;

public interface ConvertorDao {
	int createNewConvertor(Convertor convertor) throws DuplicateKeyException, Exception;

	int[] createNewConvertor(List<Convertor> convertors) throws DuplicateKeyException, Exception;

	int updateConvertor(Convertor convertor) throws DuplicateKeyException, Exception;

	int deleteConvertor(Convertor convertor) throws Exception;

	Convertor getConvertorByNumber(Convertor convertor)
			throws EmptyResultDataAccessException, DataAccessException, Exception;

	List<Convertor> getActiveConvertors() throws EmptyResultDataAccessException, DataAccessException, Exception;

	List<Convertor> getAllConvertors() throws EmptyResultDataAccessException, DataAccessException, Exception;
}
