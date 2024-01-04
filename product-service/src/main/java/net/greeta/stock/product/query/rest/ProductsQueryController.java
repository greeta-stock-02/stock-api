package net.greeta.stock.product.query.rest;

import java.util.List;

import net.greeta.stock.product.query.FindProductsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsQueryController {
	
	@Autowired
	QueryGateway queryGateway;
	
	@GetMapping
	public List<ProductRestModel> getProducts() {
		
		FindProductsQuery findProductsQuery = new FindProductsQuery();
		List<ProductRestModel> products = queryGateway.query(findProductsQuery,
				ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
		
		return products;
		
		
	}

}
