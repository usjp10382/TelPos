package com.teleios.pos.service;

import java.net.SocketTimeoutException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.teleios.pos.dao.impl.ProductDaoImpl;
import com.teleios.pos.model.Product;

@Service
public class ProductService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductDaoImpl productDaoImpl;

	public int createNewProduct(Product product) throws DuplicateKeyException, SocketTimeoutException, Exception {
		LOGGER.info("<------- Execute Create New Product In Service ---------->");
		return this.productDaoImpl.createNewProduct(product);
	}

	public int[] createNewProduct(List<Product> products)
			throws SocketTimeoutException, DuplicateKeyException, Exception {
		LOGGER.info("<------- Execute Create New Batch Of Product In Service ---------->");
		return this.productDaoImpl.createNewProduct(products);
	}

	public int updateProduct(Product product) throws DuplicateKeyException, SocketTimeoutException, Exception {
		LOGGER.info("<----- Execute Update Product Name:{} Execute In Service --->", product.getPrdName());
		return this.productDaoImpl.updateProduct(product);
	}

	public List<Product> getAllActiveProducts()
			throws SocketTimeoutException, EmptyResultDataAccessException, DataAccessException, Exception {
		LOGGER.info("<--------- Execute Get All Active Products In Service ----------->");
		return this.productDaoImpl.getAllActiveProducts();
	}
}
