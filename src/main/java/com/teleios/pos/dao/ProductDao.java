package com.teleios.pos.dao;

import java.net.SocketTimeoutException;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;

import com.teleios.pos.model.Product;

public interface ProductDao {
	int createNewProduct(Product product) throws DuplicateKeyException, SocketTimeoutException, Exception;

	int[] createNewProduct(List<Product> products) throws DuplicateKeyException, SocketTimeoutException, Exception;
}
