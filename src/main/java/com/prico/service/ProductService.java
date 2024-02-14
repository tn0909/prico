package com.prico.service;

import com.prico.dto.ProductRequest;
import com.prico.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(Long id);

    Product create(ProductRequest product);

    Product update(Long id, ProductRequest product);

    void delete(Long id);
}
