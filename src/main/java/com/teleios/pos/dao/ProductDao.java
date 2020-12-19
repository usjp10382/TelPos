package com.teleios.pos.dao;

import java.net.SocketTimeoutException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.teleios.pos.model.Product;

public interface ProductDao {
	int createNewProduct(Product product) throws SocketTimeoutException, DuplicateKeyException, Exception;

	int[] createNewProduct(List<Product> products) throws SocketTimeoutException, DuplicateKeyException, Exception;

	List<Product> getAllActiveProducts()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception;

}
