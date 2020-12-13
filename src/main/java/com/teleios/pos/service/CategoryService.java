package com.teleios.pos.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.teleios.pos.dao.impl.CategoryDaoImpl;
import com.teleios.pos.model.Category;

@Service
public class CategoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
	@Autowired
	private CategoryDaoImpl categoryDaoImpl;

	public int createNewCategory(Category category) throws DuplicateKeyException {
		LOGGER.info("Execute Create New Uom Service-----------> ");
		return this.categoryDaoImpl.createNewCategory(category);
	}

	public int updateCategory(Category category) throws DuplicateKeyException {
		LOGGER.info("Execute Update Category Service-----------> ");
		return this.categoryDaoImpl.updateCategory(category);
	}

	public int deleteCategory(Category category) {
		LOGGER.info("Execute Delete Category Service-----------> ");
		return this.categoryDaoImpl.deleteCategory(category);
	}

	public Category getCategoryByNumber(Category category) throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Category By Number Service-----------> ");
		return this.categoryDaoImpl.getCategoryByNumber(category);
	}

	public List<Category> getActiveCategories() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get Active Categories Service-----------> ");
		return this.categoryDaoImpl.getActiveCategories();
	}

	public List<Category> getAllCategories() throws EmptyResultDataAccessException, DataAccessException {
		LOGGER.info("Execute Get All Categories Service-----------> ");
		return this.categoryDaoImpl.getAllCategories();
	}

}
