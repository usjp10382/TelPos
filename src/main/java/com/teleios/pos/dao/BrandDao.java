package com.teleios.pos.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.Brand;

public interface BrandDao {
	int createNewBrand(Brand brand) throws DuplicateKeyException;

	int updateBrand(Brand brand) throws DuplicateKeyException;

	int deleteBrand(Brand brand);

	Brand getBrandByNumber(Brand brand) throws EmptyResultDataAccessException, DataAccessException;

	List<Brand> getActiveBrands() throws EmptyResultDataAccessException, DataAccessException;

	List<Brand> getAllBrands() throws EmptyResultDataAccessException, DataAccessException;
}
