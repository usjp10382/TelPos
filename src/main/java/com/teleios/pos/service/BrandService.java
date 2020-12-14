package com.teleios.pos.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.teleios.pos.dao.impl.BrandDaoImpl;
import com.teleios.pos.model.Brand;

@Service
public class BrandService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandService.class);

	@Autowired
	private BrandDaoImpl brandDaoImpl;

	public int createNewBrand(Brand brand) throws DuplicateKeyException {
		LOGGER.info("Execute Create New Brand Service-----------> ");
		return this.brandDaoImpl.createNewBrand(brand);
	}

	public int updateBrand(Brand brand) throws DuplicateKeyException {
		LOGGER.info("Execute Update Brand Service-----------> ");
		return this.brandDaoImpl.updateBrand(brand);
	}

	public int deleteBrand(Brand brand) {
		LOGGER.info("Execute Delete Brand Service-----------> ");
		return this.brandDaoImpl.deleteBrand(brand);
	}

	public Brand getBrandByNumber(Brand brand) throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Brand By Number Service-----------> ");
		return this.brandDaoImpl.getBrandByNumber(brand);
	}

	public List<Brand> getActiveBrands() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Active Brands Service-----------> ");
		return this.brandDaoImpl.getActiveBrands();
	}

	public List<Brand> getAllBrands() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get All Brands Service-----------> ");
		return this.brandDaoImpl.getAllBrands();
	}

}
