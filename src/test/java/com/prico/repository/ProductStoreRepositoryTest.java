package com.prico.repository;

import com.prico.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductStoreRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductStoreRepository productStoreRepository;

    private Product product1;

    private Product product2;

    @BeforeEach
    public void setUp() {
        // Add products
        product1 = Product
                .builder()
                .name("Yoplait Strawberry Yoghurt | 1kg")
                .description("Made in Australia and available in 4 delicious flavours.")
                .build();

        product2 = Product
                .builder()
                .name("Yoplait Petit Miam Strawberry & Banana | 70g")
                .description("Made in Australia. 4.5 health star rating.")
                .build();

        productRepository.save(product1);
        productRepository.save(product2);

        // Add stores
        Store store1 = Store
                .builder()
                .name("Woolworths")
                .build();

        Store store2 = Store
                .builder()
                .name("Coles")
                .build();

        storeRepository.save(store1);
        storeRepository.save(store2);

        // Add variations;
        ProductStore product1Store1 = ProductStore
                .builder()
                .product(product1)
                .store(store1)
                .build();

        ProductStore product1Store2 = ProductStore
                .builder()
                .product(product1)
                .store(store2)
                .build();

        productStoreRepository.save(product1Store1);
        productStoreRepository.save(product1Store2);
    }

    @Test
    public void testFindAllByProduct_HasVariations_ReturnsAll() {
        List<ProductStore> result = productStoreRepository.findAllByProduct(product1);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindAllByProduct_HasNoVariations_ReturnsEmpty() {
        List<ProductStore> result = productStoreRepository.findAllByProduct(product2);
        assertTrue(result.isEmpty());
    }
}
