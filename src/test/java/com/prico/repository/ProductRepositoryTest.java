package com.prico.repository;

import com.prico.model.Brand;
import com.prico.model.Category;
import com.prico.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        // Add brands
        Brand brand1 = Brand
                .builder()
                .name("Yoplait")
                .description("Yoplait is the world's largest franchise brand of yogurt.")
                .build();

        Brand brand2 = Brand
                .builder()
                .name("The Organic Milk Company")
                .description("The Organic Milk Company is Australian owned and operated.")
                .build();

        brandRepository.save(brand1);
        brandRepository.save(brand2);

        // Add categories
        Category category1 = Category
                .builder()
                .name("Yogurt")
                .description("A dairy product.")
                .build();

        Category category2 = Category
                .builder()
                .name("Butter & margarine")
                .description("A dairy product.")
                .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        // Add products
        Product product1 = Product
                .builder()
                .name("Yoplait Strawberry Yoghurt | 1kg")
                .description("Made in Australia and available in 4 delicious flavours.")
                .brand(brand1)
                .category(category1)
                .build();

        Product product2 = Product
                .builder()
                .name("Yoplait Petit Miam Strawberry & Banana | 70g")
                .description("Made in Australia. 4.5 health star rating.")
                .brand(brand1)
                .category(category1)
                .build();

        Product product3 = Product
                .builder()
                .name("The Organic Milk Company Salted Butter | 250g")
                .description("No additives, antibiotics, or GMOs.")
                .brand(brand2)
                .category(category2)
                .build();

        repository.save(product1);
        repository.save(product2);
        repository.save(product3);
    }

    @Test
    public void testSearch() {
        List<Product> result = repository.search("banana", "yogurt", "yoplait");
        assertEquals(1, result.size());
    }

    @Test
    public void testSearch_WithoutName() {
        List<Product> result = repository.search(null,"butter", "organic milk");
        assertEquals(1, result.size());
    }

    @Test
    public void testSearch_WithoutCategory() {
        List<Product> result = repository.search("banana","", "yoplait");
        assertEquals(1, result.size());
    }

    @Test
    public void testSearch_WithoutBrand() {
        List<Product> result = repository.search("strawberry","yogurt", null);
        assertEquals(2, result.size());
    }
}
