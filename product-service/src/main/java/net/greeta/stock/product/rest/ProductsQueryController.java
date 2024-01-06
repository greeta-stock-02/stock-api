package net.greeta.stock.product.rest;

import java.util.List;

import net.greeta.stock.product.dto.ProductDto;
import net.greeta.stock.product.query.FindProductQuery;
import net.greeta.stock.product.query.FindProductsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsQueryController {
	
	@Autowired
	QueryGateway queryGateway;
	
	@GetMapping
	public List<ProductDto> getProducts() {
		FindProductsQuery findProductsQuery = new FindProductsQuery();
		List<ProductDto> products = queryGateway.query(findProductsQuery,
				ResponseTypes.multipleInstancesOf(ProductDto.class)).join();
		return products;
	}

	@GetMapping(path = "/{productId}")
	public ProductDto getProduct(@PathVariable String productId) {
		FindProductQuery findProductQuery = new FindProductQuery(productId);
		ProductDto product = queryGateway.query(findProductQuery, ProductDto.class).join();
		return product;
	}

}
