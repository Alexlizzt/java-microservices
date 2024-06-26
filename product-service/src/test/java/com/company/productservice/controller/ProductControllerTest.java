package com.company.productservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.company.productservice.data.DummyData.*;

import com.company.productservice.entity.ProductEntity;
import com.company.productservice.repository.ProductRepository;

import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.stream.StreamSupport;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
	
	@Mock
	private ProductRepository productRepository;

	private ProductEntity product;
	
	@InjectMocks
	private ProductEntity productEntity;
	
	@BeforeEach
	void setUp() throws Exception {
		product = product01();
	}

	@DisplayName("Test para listar todos los productos")
	@Test
	void testGetAllProducts() {
		// Given
        ProductEntity product1 =  product02();
        given(productRepository.findAll()).willReturn(List.of(product,product1));

        // When
        List<ProductEntity> products =
                StreamSupport.stream(productRepository.findAll().spliterator(), false).toList();

        // Then
        assertThat(product).isNotNull();
        assertThat(products.size()).isEqualTo(2);
	}

	@DisplayName("Test para guardar un producto")
	@Test
	void testCreateProduct() {
		// Given
        given(productRepository.save(product)).willReturn(product);

        // When
        ProductEntity savedProduct = productRepository.save(product);

        // Then
        assertThat(savedProduct).isNotNull();
	}

}
