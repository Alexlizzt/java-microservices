package com.company.productservice.data;

import com.company.productservice.entity.ProductEntity;

public class DummyData {
	public static ProductEntity product01() {
		return new ProductEntity(null, "Laptop", "Most popular product", 4200.0);
	}
	
	public static ProductEntity product02() {
		return new ProductEntity(null, "SmartPhone", "Everyone wants one", 2150.0);
	}
}
