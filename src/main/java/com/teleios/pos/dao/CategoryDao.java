package com.teleios.pos.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.Category;

public interface CategoryDao {
	int createNewCategory(Category category) throws DuplicateKeyException;

	int updateCategory(Category category) throws DuplicateKeyException;

	int deleteCategory(Category category);

	Category getCategoryByNumber(Category category) throws EmptyResultDataAccessException, DataAccessException;

	List<Category> getActiveCategories() throws EmptyResultDataAccessException, DataAccessException;

	List<Category> getAllCategories() throws EmptyResultDataAccessException, DataAccessException;
}
