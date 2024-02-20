package com.prico.repository;

import com.prico.model.Product;
import com.prico.model.ProductStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductStoreRepository extends JpaRepository<ProductStore, Long> {

    List<ProductStore> findAllByProduct(Product product);
}
