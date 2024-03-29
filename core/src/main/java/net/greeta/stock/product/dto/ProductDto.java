package net.greeta.stock.product.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductDto {
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
}
