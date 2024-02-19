package com.prico.repository;

import com.prico.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
            "JOIN p.category c " +
            "JOIN p.brand b " +
            "WHERE LOWER(p.name) LIKE %:name% " +
            "AND (:category IS NULL OR LOWER(c.name) LIKE %:category%) " +
            "AND (:brand IS NULL OR LOWER(b.name) LIKE %:brand%)")
    List<Product> searchProducts(String name, String category, String brand);
}
